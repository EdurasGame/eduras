package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.utils.Pair;

/**
 * Listens on Port {@value #SERVER_PORT} to receive UDP broadcast packages.
 * 
 * @author illonis
 * 
 */
public class ServerDiscoveryListener extends Thread {
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
		EduLog.info("ServerSearcher is starting to listen for UDP-Broadcasts on port "
				+ SERVER_PORT + ".");

		// prepare answer data:
		String answer = ServerDiscoveryListener.ANSWER_MSG + "#" + name + "#"
				+ port;

		try {
			// Keep a socket open to listen to all the UDP traffic that is
			// destined for this port
			channel = new DiscoveryChannel(true);

			InetSocketAddress listenAddress = new InetSocketAddress(
					InetAddress.getByName("0.0.0.0"), SERVER_PORT);
			channel.bind(listenAddress);

			Pair<SocketAddress, String> returnData = null;

			while (true) {
				// Receive a packet
				returnData = channel.receive();
				if (returnData == null)
					continue;

				InetSocketAddress isa = (InetSocketAddress) returnData
						.getFirst();

				String message = returnData.getSecond();
				// Packet received
				EduLog.info("Discovery packet received from: "
						+ isa.getAddress().getHostAddress() + ", Data: "
						+ message);

				// See if the packet holds the right command (message)
				if (message.equals(ServerDiscoveryListener.REQUEST_MSG)) {
					isa = new InetSocketAddress(isa.getAddress(), CLIENT_PORT);
					// Send a response
					channel.send(answer, isa);
					EduLog.info("Sent packet to: "
							+ isa.getAddress().getHostAddress());
				} else {
					EduLog.warning("Received invalid broadcast message.");
				}
			}
		} catch (IOException ex) {
			EduLog.passException(ex);
		}
	}
}
