package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AlreadyBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.utils.Pair;

/**
 * Listens on Port {@value #SERVER_PORT} to receive UDP broadcast packages.
 * 
 * @author illonis
 * 
 */
public class ServerDiscoveryListener extends Thread {

	private final static Logger L = EduLog
			.getLoggerFor(ServerDiscoveryListener.class.getName());
	/*
	 * Main idea of this code is from Michiel De Mey and has been modified to
	 * fit to Eduras?. Source:
	 * http://michieldemey.be/blog/network-discovery-using-udp-broadcast/
	 */

	/**
	 * The port where discovery server listens on.
	 */
	public final static int SERVER_PORT = 9876;

	/**
	 * The port where the meta server listens on.
	 */
	public final static int META_SERVER_PORT = 9877;

	/**
	 * The port where discovery client receives answer on.
	 */
	public final static int CLIENT_PORT = 9875;
	/**
	 * The message that is send for discovery.
	 */
	public final static String REQUEST_MSG = "EDURAS_SERVER_REQUEST";
	/**
	 * The answer message.
	 */
	public final static String ANSWER_MSG = "EDURAS_SERVER_ANSWER";

	private final String name;
	private final int port;

	private DiscoveryChannel channel;

	/**
	 * Creates a new server that listens for discovery requests from UDP.
	 * 
	 * @param serverName
	 *            name of this server.
	 * @param serverPort
	 *            port this server allows connections on.
	 */
	public ServerDiscoveryListener(String serverName, int serverPort) {
		super("ServerDiscoveryListener");
		this.name = serverName;
		this.port = serverPort;
	}

	@Override
	public void run() {
		L.info("ServerSearcher is starting to listen for UDP-Broadcasts on port "
				+ SERVER_PORT + ".");

		// listen on both the UDP port for LAN and metaserver
		ServerDiscoveryListenerForAPort metaServerListener = new ServerDiscoveryListenerForAPort(
				port + 2);
		ServerDiscoveryListenerForAPort lanListener = new ServerDiscoveryListenerForAPort(
				SERVER_PORT);

		metaServerListener.start();
		lanListener.start();

		try {
			metaServerListener.join();
			lanListener.join();
		} catch (InterruptedException e) {
			L.log(Level.SEVERE,
					"Interrupted while waiting on discovery listener threads.",
					e);
		}
	}

	class ServerDiscoveryListenerForAPort extends Thread {
		private int myPort;

		public ServerDiscoveryListenerForAPort(int portToListen) {
			myPort = portToListen;
		}

		@Override
		public void run() {
			String answer = ServerDiscoveryListener.ANSWER_MSG + "#" + name
					+ "#" + port;

			// Keep a socket open to listen to all the UDP traffic that is
			// destined for this port
			L.info("Initializing DiscoveryListening on port " + myPort);
			boolean bound = false;
			int cntAttempts = 0;
			while (!bound) {
				try {
					channel = new DiscoveryChannel(true);
					InetSocketAddress listenAddress = new InetSocketAddress(
							InetAddress.getByName("0.0.0.0"), myPort);
					channel.bind(listenAddress);
				} catch (IOException | AlreadyBoundException ex) {
					cntAttempts++;
					L.log(Level.WARNING, "Error binding to port " + myPort
							+ " after try " + cntAttempts, ex);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						L.log(Level.WARNING, "Cannot sleep!?", e);
						continue;
					}
					continue;
				}
				bound = true;
				L.info("Successfully initialized DiscoveryListening on port "
						+ myPort);
			}

			Pair<SocketAddress, String> returnData = null;

			while (true) {
				// Receive a packet
				try {
					returnData = channel.receive();
					if (returnData == null)
						continue;

					InetSocketAddress isa = (InetSocketAddress) returnData
							.getFirst();

					String message = returnData.getSecond();
					// Packet received
					L.info("Discovery packet received from: "
							+ isa.getAddress().getHostAddress() + ":"
							+ isa.getPort() + ", Data: " + message);

					// See if the packet holds the right command (message)
					if (message.equals(ServerDiscoveryListener.REQUEST_MSG)) {
						// Send a response
						channel.send(answer, isa);
						L.info("Sent packet to: "
								+ isa.getAddress().getHostAddress());
					} else {
						L.warning("Received invalid broadcast message.");
					}
				} catch (IOException e) {
					L.log(Level.SEVERE, "Error in discovery", e);
				}
			}

		}
	}
}
