package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.Client;
import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
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
	private MetaServerRequester metaserverRequester;

	/**
	 * Broadcast interval in milliseconds.
	 */
	public final static int BROADCAST_INTERVAL = 2000;

	/**
	 * The address of the meta server.
	 */
	public final static String METASERVER_ADDRESS = "localhost";

	/**
	 * Creates a new server searcher.
	 * 
	 * @param answerListener
	 *            the listener object that receives the found servers.
	 */
	public ServerSearcher(ServerFoundListener answerListener) {
		super("ServerSearcher");
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
		c.send("##" + ServerDiscoveryListener.REQUEST_MSG + "##", target);
		L.fine("[ServerSearcher] Sent request packet to "
				+ target.getAddress().getHostAddress() + ":" + target.getPort()
				+ ".");

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
		L.info("Connecting to MetaServer.");
		metaserverRequester = new MetaServerRequester();

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
			interrupt();
			return false;
		}
		return true;
	}

	class MetaServerRequester implements ClientNetworkEventHandler,
			EventHandler {

		private final ClientInterface client;

		public MetaServerRequester() {
			client = new Client();
			client.setNetworkEventHandler(MetaServerRequester.this);
			client.setEventHandler(MetaServerRequester.this);
			client.connect(METASERVER_ADDRESS,
					ServerDiscoveryListener.META_SERVER_PORT);
		}

		@Override
		public void onClientDisconnected(int clientId) {
			// don't care
		}

		@Override
		public void onClientConnected(int clientId) {
			// don't care
		}

		@Override
		public void handleEvent(Event event) {
			switch (event.getEventNumber()) {
			case MetaServer.GET_SERVERS_RESPONSE: {
				L.fine("Received metaserver answer.");
				int numberOfEdurasServers;
				try {
					numberOfEdurasServers = (Integer) event.getArgument(0);

					for (int i = 1; i < numberOfEdurasServers * 3; i = i + 3) {
						String nameOfEdurasServer = (String) event
								.getArgument(i);
						String ipOfEdurasServer = (String) event
								.getArgument(i + 1);
						int portOfEdurasServer = (Integer) event
								.getArgument(i + 2);

						try {
							ServerInfo serverInfo = new ServerInfo(
									nameOfEdurasServer,
									InetAddress.getByName(ipOfEdurasServer),
									portOfEdurasServer);
							listener.onServerFound(serverInfo);
						} catch (UnknownHostException e) {
							L.log(Level.WARNING,
									"Cannot generate InetAddress out of IP "
											+ ipOfEdurasServer, e);
							continue;
						}
					}

				} catch (TooFewArgumentsExceptions e1) {
					L.log(Level.WARNING,
							"Error accessing arguments of GET_SERVERS_RESPONSE",
							e1);
					return;
				}
			}
				break;
			}

		}

		@Override
		public void onConnectionLost() {
			L.warning("Lost connection to MetaServer");
		}

		@Override
		public void onDisconnected() {
			L.warning("Disconnected from MetaServer");
		}

		@Override
		public void onClientKicked(int clientId, String reason) {
			// doesn't appear
		}

		@Override
		public void onServerIsFull() {
			// doesn't appear
		}

		@Override
		public void onPingReceived(long latency) {
			// doesn't appear
		}

		public void stopServer() {
			client.disconnect();
		}

		public void request() {
			Event requestEvent = new Event(MetaServer.GET_SERVERS_REQUEST);
			try {
				requestEvent.putArgument(client.getClientId());
				client.sendEvent(requestEvent);
			} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
				L.log(Level.WARNING, "Error on sending GET_SERVERS_REQUEST.", e);
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
				interrupt();
				break;
			}
		}

		c.close();
	}

	private void sendMetaServerRequest() {
		if (metaserverRequester == null) {
			return;
		}

		metaserverRequester.request();
	}

	@Override
	public void interrupt() {
		c.close();
		if (handler != null)
			handler.interrupt();
		if (metaserverRequester != null)
			metaserverRequester.stopServer();
		super.interrupt();
	}
}
