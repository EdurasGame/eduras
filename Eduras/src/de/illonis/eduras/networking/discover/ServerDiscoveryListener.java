package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import de.illonis.eduras.logger.EduLog;

/**
 * Listens on Port {@value #DISCOVERY_PORT} to receive UDP broadcast packages.
 * 
 * @author illonis
 * 
 */
public class ServerDiscoveryListener extends Thread {
	/*
	 * Most parts of this code are written by Michiel De Mey and modified
	 * slightly. Source:
	 * http://michieldemey.be/blog/network-discovery-using-udp-broadcast/
	 */

	/**
	 * The port where server listens on.
	 */
	public final static int DISCOVERY_PORT = 8555;
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

	private DatagramSocket socket;

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
		EduLog.info("ServerSearcher is starting to listen for UDP-Broadcasts.");
		try {
			// Keep a socket open to listen to all the UDP trafic that is
			// destined for this port
			socket = new DatagramSocket(ServerDiscoveryListener.DISCOVERY_PORT,
					InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (true) {
				// Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf,
						recvBuf.length);
				socket.receive(packet);

				// Packet received
				System.out.println(getClass().getName()
						+ ">>>Discovery packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println(getClass().getName()
						+ ">>>Packet received; data: "
						+ new String(packet.getData()));

				// See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				if (message.equals(ServerDiscoveryListener.REQUEST_MSG)) {
					byte[] sendData = (ServerDiscoveryListener.ANSWER_MSG + "#"
							+ name + "#" + port).getBytes();

					// Send a response
					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, packet.getAddress(),
							packet.getPort());
					socket.send(sendPacket);

					System.out.println(getClass().getName()
							+ ">>>Sent packet to: "
							+ sendPacket.getAddress().getHostAddress());
				}
			}
		} catch (IOException ex) {
			EduLog.passException(ex);
		}
	}
}
