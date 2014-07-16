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
import de.illonis.eduras.chat.NotConnectedException;

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
	public final static String METASERVER_ADDRESS = "ren-mai.net";

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

			sendRequestTo(target);
			L.fine("[ServerSearcher] Sent request packet via 255.255.255.255.");
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
		try {
			metaserverRequester = new MetaServerRequester();
		} catch (NotConnectedException e1) {
			L.log(Level.WARNING,
					"Cannot connect to MetaServer. Discovery via Internet won't work.",
					e1);
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
			interrupt();
			return false;
		}
		return true;
	}

	class MetaServerRequester implements ClientNetworkEventHandler,
			EventHandler {

		private final ClientInterface client;
		private boolean connectionEstablished;

		public MetaServerRequester() throws NotConnectedException {
			client = new Client();
			client.setNetworkEventHandler(MetaServerRequester.this);
			client.setEventHandler(MetaServerRequester.this);
			connectionEstablished = false;
			client.connect(METASERVER_ADDRESS,
					ServerDiscoveryListener.META_SERVER_PORT);

			if (!client.isConnected()) {
				throw new NotConnectedException();
			}

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
				int numberOfEdurasServers;
				try {
					numberOfEdurasServers = (Integer) event.getArgument(0);
					L.fine("Received metaserver GET_SERVERS_RESPONSE. It reported "
							+ numberOfEdurasServers + " servers.");

					for (int i = 1; i < numberOfEdurasServers * 4; i = i + 4) {
						try {
							ServerInfo info = parseServerInfo(event, i);
							listener.onServerFound(info);
						} catch (UnknownHostException e) {
							L.log(Level.WARNING, "", e);
						}
					}

				} catch (TooFewArgumentsExceptions e1) {
					L.log(Level.WARNING,
							"Error accessing arguments of GET_SERVERS_RESPONSE",
							e1);
					return;
				}
				break;
			}
			default:
				L.warning("Received event we cannot handle. Eventnumber: "
						+ event.getEventNumber());
			}

		}

		private ServerInfo parseServerInfo(Event event, int i)
				throws TooFewArgumentsExceptions, UnknownHostException {
			String nameOfEdurasServer = (String) event.getArgument(i);
			String ipOfEdurasServer = (String) event.getArgument(i + 1);
			int portOfEdurasServer = (Integer) event.getArgument(i + 2);
			String versionOfEdurasServer = (String) event.getArgument(i + 3);
			int numberOfPlayers = (Integer) event.getArgument(i + 4);
			String gameMode = (String) event.getArgument(i + 5);
			String map = (String) event.getArgument(i + 6);

			L.info("Metaserver reported the following server. name : "
					+ nameOfEdurasServer + " ; address : " + ipOfEdurasServer
					+ ":" + portOfEdurasServer + " ; version : "
					+ versionOfEdurasServer + " ; players: " + numberOfPlayers
					+ " ; game mode :" + gameMode + " ; map: " + map);

			try {
				ServerInfo serverInfo = new ServerInfo(nameOfEdurasServer,
						InetAddress.getByName(ipOfEdurasServer),
						portOfEdurasServer, versionOfEdurasServer,
						numberOfPlayers, gameMode, map);
				return serverInfo;
			} catch (UnknownHostException e) {
				throw new UnknownHostException("Cannot genereate IP out of "
						+ ipOfEdurasServer + ". " + e.getMessage());
			}
		}

		@Override
		public void onConnectionLost() {
			L.warning("Lost connection to MetaServer");
		}

		@Override
		public void onDisconnected() {
			L.info("Disconnected from MetaServer");
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

		@Override
		public void onConnectionEstablished(int clientId) {
			connectionEstablished = true;
		}

		public boolean isConnected() {
			return connectionEstablished;
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

			if (metaserverRequester != null
					&& metaserverRequester.isConnected()) {
				sendMetaServerRequest();
			}
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
