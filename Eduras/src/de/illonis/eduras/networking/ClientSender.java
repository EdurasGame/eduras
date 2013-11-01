package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ConnectionLostException;
import de.illonis.eduras.locale.Localization;

/**
 * Sends messages/events to the server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientSender {
	private final static Logger L = EduLog.getLoggerFor(ClientSender.class
			.getName());

	private Socket socket = null;
	private boolean active;
	private PrintWriter messageWriter = null;
	private DatagramSocket udpSocket;

	/**
	 * The transport layer type of a packet.
	 * 
	 * @author Florian Mai <florian.ren.mai@googlemail.com>
	 * 
	 */
	public enum PacketType {
		/**
		 * UDP
		 */
		UDP,
		/**
		 * TCP
		 */
		TCP;
	}

	/**
	 * Creates a new ClientSender that sends messages via the given socket.
	 * 
	 * @param socket
	 *            The socket to send messages via.
	 */
	public ClientSender(Socket socket) {

		this.socket = socket;
		active = true;
		try {
			this.messageWriter = new PrintWriter(this.socket.getOutputStream(),
					true);
		} catch (IOException e) {
			active = false;
			L.log(Level.SEVERE, "error creating writer", e);
		}

	}

	/**
	 * Sends a message via the socket if available.
	 * 
	 * @param message
	 *            The message to send.
	 * @param packetType
	 *            tells if the message is a UDP or TCP message.
	 * @throws ConnectionLostException
	 *             when connection to server is lost. The client sender will not
	 *             accept any messages anymore.
	 */
	public void sendMessage(String message, PacketType packetType)
			throws ConnectionLostException {
		if (active) {
			switch (packetType) {
			case TCP:
				L.info("[CLIENT] Sending message: " + message);
				messageWriter.println(message);
				if (messageWriter.checkError()) {
					L.severe(Localization
							.getString("Client.networking.senderror"));
					active = false;
					close();
					throw new ConnectionLostException();
				}
				break;
			case UDP:
				byte[] data = message.getBytes();
				InetSocketAddress address = new InetSocketAddress(
						socket.getInetAddress(), socket.getPort());
				DatagramPacket udpPacket;
				try {
					udpPacket = new DatagramPacket(data, data.length, address);
					udpSocket.send(udpPacket);
				} catch (IOException e) {
					L.log(Level.SEVERE, "error sending udp", e);
					active = false;
					close();
					throw new ConnectionLostException();
				}
				break;
			default:
			}

		}
	}

	/**
	 * Closes the output connection.
	 */
	public void close() {
		messageWriter.close();
	}

	/**
	 * Sets the socket on which UDP messages are sent.
	 * 
	 * @param udpSocket2
	 *            The socket to send UDP messages on.
	 */
	public void setUdpSocket(DatagramSocket udpSocket2) {
		udpSocket = udpSocket2;

	}

}
