package de.illonis.eduras.networking.discover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Searches for servers in local network using UDP broadcasts.
 * 
 * @author illonis
 * 
 */
public class ServerSearcher extends Thread {

	private final static Logger L = EduLog.getLoggerFor(ServerSearcher.class
			.getName());

	private DiscoveryChannel c;
	private ClientServerResponseHandler handler;
	private ServerFoundListener listener;
	private PrintWriter metaServerWriter;
	private MetaServerAnswerListener metaAnswerListener;
	private Socket socketToMetaServer;

	/**
	 * Broadcast interval in milliseconds.
	 */
	public final static int BROADCAST_INTERVAL = 2000;

	/**
	 * The address of the meta server.
	 */
	public final static String METASERVER_ADDRESS = "illonis.dyndns.org";

	/**
	 * Creates a new server searcher. The listener must be applied later before
	 * starting.
	 */
	public ServerSearcher() {
		super("ServerSearcher");
	}

	/**
	 * Creates a new server searcher.
	 * 
	 * @param answerListener
	 *            the listener object that receives the found servers.
	 */
	public ServerSearcher(ServerFoundListener answerListener) {
		this();
		setName(getClass().getName());
		this.listener = answerListener;
	}

	/**
	 * Sets the listener.
	 * 
	 * @param listener
	 *            new listener.
	 */
	public void setListener(ServerFoundListener listener) {
		this.listener = listener;
	}

	private void sendDiscoverBroadcast() {

		// Find the server using UDP broadcast
		try {
			InetSocketAddress target = new InetSocketAddress(
					InetAddress.getByName("255.255.255.255"),
					ServerDiscoveryListener.SERVER_PORT);

			c.send(ServerDiscoveryListener.REQUEST_MSG, target);
			L.info("[ServerSearcher] Sent request packet via 255.255.255.255.");
			// Broadcast the message over all the network interfaces
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
					// Don't want to broadcast to the loopback interface
				}

				for (InterfaceAddress interfaceAddress : networkInterface
						.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					target = new InetSocketAddress(broadcast.getHostAddress(),
							ServerDiscoveryListener.SERVER_PORT);

					// Send the broadcast package!
					sendRequestTo(target);
				}
			}
		} catch (IOException e) {
			L.log(Level.SEVERE, "error sending request", e);
		}
	}

	private void sendRequestTo(InetSocketAddress target) throws IOException {
		c.send(ServerDiscoveryListener.REQUEST_MSG, target);
		L.info("[ServerSearcher] Sent request packet to "
				+ target.getAddress().getHostAddress() + " .");

	}

	private boolean init() {
		try {
			// Open a random port to send the package
			c = new DiscoveryChannel(false);
			c.bind(new InetSocketAddress(ServerDiscoveryListener.CLIENT_PORT));

		} catch (IOException e) {
			L.log(Level.SEVERE, "error binding socket", e);
			listener.onDiscoveryFailed();
			c.close();
			return false;
		}

		// Open TCP connection to meta server
		metaServerWriter = null;
		try {
			socketToMetaServer = new Socket();
			socketToMetaServer.connect(new InetSocketAddress(
					METASERVER_ADDRESS,
					ServerDiscoveryListener.META_SERVER_PORT), 1000);
			metaServerWriter = new PrintWriter(
					socketToMetaServer.getOutputStream(), true);

			metaAnswerListener = new MetaServerAnswerListener(
					new BufferedReader(new InputStreamReader(
							socketToMetaServer.getInputStream())));

			metaAnswerListener.start();

		} catch (IOException e) {
			L.log(Level.WARNING, "Cannot connect to meta server.", e);
			// if there's no internet connection, thats okay.
		}

		handler = new ClientServerResponseHandler(listener, c);
		handler.start();

		try {
			synchronized (handler) {
				// wait for response handler to be ready.
				// this is required so we do not receive anything before we
				// listen to it.
				handler.wait();
			}
		} catch (InterruptedException e) {
			L.log(Level.WARNING, "interrupted waiting for response", e);
			return false;
		}
		return true;
	}

	class MetaServerAnswerListener extends Thread {

		private BufferedReader inputReader;

		public MetaServerAnswerListener(BufferedReader input) {
			inputReader = input;
		}

		@Override
		public void run() {
			while (true) {
				try {
					String answer = inputReader.readLine();
					processAnswer(answer);
				} catch (IOException e) {
					L.log(Level.SEVERE, "error reading answer", e);
					return;
				}
			}
		}

		private void processAnswer(String answer) {
			if (!answer.contains(MetaServer.META_SERVER_ANSWER))
				return;

			String[] ipAddresses = answer.split("#");

			for (String singleAddress : ipAddresses) {
				if (!singleAddress.equals(MetaServer.META_SERVER_ANSWER))
					try {
						sendRequestTo(new InetSocketAddress(singleAddress,
								ServerDiscoveryListener.SERVER_PORT));
					} catch (IOException e) {
						L.log(Level.SEVERE, "error sending request", e);
						continue;
					}
			}
		}
	}

	@Override
	public void run() {
		if (listener == null) {
			throw new IllegalStateException(
					"There is no listener attached to ServerSearcher.");
		}

		if (!init())
			return;

		while (true) {
			sendDiscoverBroadcast();
			sendMetaServerRequest();
			try {
				Thread.sleep(BROADCAST_INTERVAL);
			} catch (InterruptedException e) {
				break;
			}
		}

		c.close();
	}

	private void sendMetaServerRequest() {
		if (metaServerWriter == null) {
			return;
		}

		metaServerWriter.println(MetaServer.GET_SERVERS_REQUEST);
	}

	@Override
	public void interrupt() {
		c.close();
		try {
			if (socketToMetaServer != null)
				socketToMetaServer.close();
		} catch (IOException e) {
			L.log(Level.WARNING, "error closing socket", e);
		}
		if (handler != null)
			handler.interrupt();
		if (metaAnswerListener != null)
			metaAnswerListener.interrupt();
		super.interrupt();
	}

}
