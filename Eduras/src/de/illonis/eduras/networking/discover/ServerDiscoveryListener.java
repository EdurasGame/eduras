package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AlreadyBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasVersion;
import de.illonis.eduras.GameInformation;
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

	private final int CONNECT_ATTEMPTS = 10;

	private final String name;
	private final int port;
	private final GameInformation gameInfo;

	private DiscoveryChannel channel;

	/**
	 * Creates a new server that listens for discovery requests from UDP.
	 * 
	 * @param serverName
	 *            name of this server.
	 * @param serverPort
	 *            port this server allows connections on.
	 * @param gameInfo
	 *            the gameinfo to derive number of players, gamemode and map
	 *            from
	 */
	public ServerDiscoveryListener(String serverName, int serverPort,
			GameInformation gameInfo) {
		super("ServerDiscoveryListener");
		this.name = serverName;
		this.port = serverPort;
		this.gameInfo = gameInfo;
	}

	@Override
	public void run() {
		L.info("ServerSearcher is starting to listen for UDP-Broadcasts on port "
				+ SERVER_PORT + ".");

		// listen on the UDP port for LAN
		ServerDiscoveryListenerForAPort lanListener = new ServerDiscoveryListenerForAPort(
				SERVER_PORT);

		lanListener.start();

		try {
			lanListener.join();
		} catch (InterruptedException e) {
			L.log(Level.SEVERE,
					"Interrupted while waiting on discovery listener threads.",
					e);
		}
	}

	class ServerDiscoveryListenerForAPort extends Thread {
		private final int myPort;

		public ServerDiscoveryListenerForAPort(int portToListen) {
			myPort = portToListen;
		}

		@Override
		public void run() {
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

					if (cntAttempts > CONNECT_ATTEMPTS) {
						L.warning("Could not connect after 10 attempts. Will not support discovery.");
						return;
					}

					L.log(Level.INFO, "Error binding to port " + myPort
							+ " after try " + cntAttempts, ex);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						L.log(Level.WARNING, "Cannot sleep!?", e);
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

					String[] singleMessages = message.split("##");

					for (String aSingleMessage : singleMessages) {
						parseAndAnswerMessage(aSingleMessage, isa);
					}

				} catch (IOException e) {
					L.log(Level.SEVERE, "Error in discovery", e);
				}
			}

		}

		private void parseAndAnswerMessage(String aSingleMessage,
				InetSocketAddress isa) {
			// See if the packet holds the right command (message)
			if (aSingleMessage.equals(ServerDiscoveryListener.REQUEST_MSG)) {
				String answer = createDiscoveryAnswer(name, port, gameInfo);
				// Send a response
				try {
					channel.send(answer, isa);
				} catch (IOException e) {
					L.log(Level.WARNING, "Error in discovery", e);
					return;
				}
				L.fine("Sent packet to: " + isa.getAddress().getHostAddress()
						+ ":" + isa.getPort() + ". Message: " + answer);
			} else {
				L.warning("Received invalid broadcast message:"
						+ aSingleMessage);
			}

		}
	}

	/**
	 * Creates a server's discovery answer.
	 * 
	 * @param name
	 *            The server's name to answer with
	 * @param port
	 *            The port that server is running on
	 * @param gameInfo
	 *            game information to derive the number of players, gamemode and
	 *            map from
	 * @return the answer as string
	 */
	public static String createDiscoveryAnswer(String name, int port,
			GameInformation gameInfo) {
		return "##" + ServerDiscoveryListener.ANSWER_MSG + "#" + name + "#"
				+ port + "#" + EdurasVersion.getVersion() + "#"
				+ gameInfo.getPlayers().size() + "#"
				+ gameInfo.getGameSettings().getGameMode().getName() + "#"
				+ gameInfo.getMap().getName() + "##";
	}
}
