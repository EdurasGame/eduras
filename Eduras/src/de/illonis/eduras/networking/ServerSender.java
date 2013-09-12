package de.illonis.eduras.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import de.illonis.eduras.events.Event;
import de.illonis.eduras.exceptions.BufferIsEmptyException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.networking.ClientSender.PacketType;

/**
 * A class that sends collected messages every {@value #SEND_INTERVAL} ms.
 * 
 * @author illonis
 * 
 */
public class ServerSender extends Thread {

	/**
	 * Message send interval
	 */
	private final static int SEND_INTERVAL = 33;

	private final HashMap<Integer, ServerClient> clients;
	private DatagramSocket udpSocket;
	private final Buffer outputBufferUDP;
	private final Buffer outputBufferTCP;
	private final Server server;
	private boolean running;
	private NetworkPolicy networkPolicy;

	/**
	 * Creates a new ServerSender that sends messages from given Buffer.
	 * 
	 * @param server
	 *            Target server.
	 */
	public ServerSender(Server server) {
		this.outputBufferUDP = new Buffer();
		this.outputBufferTCP = new Buffer();
		try {
			this.udpSocket = new DatagramSocket();
		} catch (SocketException e) {
			EduLog.passException(e);
			server.stopServer();
		}
		this.setName("ServerSender");
		clients = new HashMap<Integer, ServerClient>();
		this.server = server;
		running = true;
		// TODO: Change this
		networkPolicy = new InetPolizei();
	}

	/**
	 * Sends a serialized message to all receivers as TCP message.
	 * 
	 * @param message
	 *            Message to send.
	 */
	private void sendTCPMessage(String message) {
		for (ServerClient serverClient : clients.values()) {
			PrintWriter pw = serverClient.getOutputStream();
			pw.println(message);
			EduLog.info("Server.networking.msgsend");
		}
	}

	/**
	 * Sends a serialized message to all receivers as UDP message.
	 * 
	 * @param message
	 */
	private void sendUDPMessage(String message) {
		for (ServerClient serverClient : clients.values()) {
			byte[] messageAsBytes = message.getBytes();
			InetAddress clientaddress = serverClient.getSocket()
					.getInetAddress();
			int port = serverClient.getSocket().getPort();
			DatagramPacket packet = new DatagramPacket(messageAsBytes,
					messageAsBytes.length, clientaddress, port);
			try {
				udpSocket.send(packet);
			} catch (IOException e) {
				EduLog.passException(e);
			}
		}
	}

	/**
	 * Sends a message to a single client identified by its id.
	 * 
	 * @param clientId
	 *            The id of the client.
	 * @param message
	 *            the serialized message that should be sent.
	 * @param packetType
	 *            Tells whether the message is sent via UDP or TCP
	 */
	private void sendMessageToClient(int clientId, String message,
			PacketType packetType) {
		if (packetType == PacketType.TCP) {
			PrintWriter pw = clients.get(clientId).getOutputStream();
			pw.println(message);
		} else {
			ServerClient serverClient = clients.get(clientId);
			byte[] messageAsBytes = message.getBytes();
			InetAddress clientaddress = serverClient.getSocket()
					.getInetAddress();
			int port = serverClient.getSocket().getPort();
			DatagramPacket packet = new DatagramPacket(messageAsBytes,
					messageAsBytes.length, clientaddress, port);
			try {
				udpSocket.send(packet);
			} catch (IOException e) {
				EduLog.passException(e);
			}

		}

	}

	/**
	 * Creates ServerClient of given socket and adds it to senderlist so it
	 * reveives messages from server.
	 * 
	 * @param client
	 *            Socket of the client to add.
	 * @return Returns the created ServerClient.
	 * @throws IOException
	 *             Is thrown if the socket is somehow broken.
	 */
	public ServerClient add(Socket client) throws IOException {
		int clientId = getFreeClientId();

		ServerClient serverClient = new ServerClient(clientId, client);
		clients.put(clientId, serverClient);
		return serverClient;

	}

	/**
	 * Returns a number that has not been assigned to any client currently
	 * connected.
	 * 
	 * @return The free number.
	 * 
	 *         Returns -1 if there cannot be found a number within INT_MAX. I
	 *         guess this will never ever happen.
	 */
	private int getFreeClientId() {

		for (int i = 0; i >= 0; i++) {
			if (!clients.containsKey(new Integer(i))) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Removes given client from senderlist so it does not receive any messages.
	 * 
	 * @param client
	 *            Client to remove.
	 */
	public void remove(ServerClient client) {
		clients.remove(client.getClientId());
	}

	@Override
	public void run() {
		while (running) {
			// if (System.currentTimeMillis() - lastConnectionCheck > 10000) {
			// checkConnections();
			// }
			sendAllMessages();
			try {
				Thread.sleep(SEND_INTERVAL);
			} catch (InterruptedException e) {
				EduLog.passException(e);
			}
		}
	}

	/**
	 * Retrieves all messages from outputBuffer and sends them to all clients.
	 */
	private void sendAllMessages() {
		sendTCPBufferContent();
		sendUDPBufferContent();
	}

	private void sendTCPBufferContent() {
		try {
			String[] s = outputBufferTCP.getAll();
			String[] filtereds = NetworkOptimizer.filterObsoleteMessages(s);
			String message = NetworkMessageSerializer.concatenate(filtereds);

			if (message.equals("")) {
				EduLog.warning("Message empty!!");
				return;
			}
			EduLog.info("[SERVER] Sent all messages.");
			sendTCPMessage(message);
		} catch (BufferIsEmptyException e) {
			// do nothing if there is no message.
		}
	}

	private void sendUDPBufferContent() {
		try {
			String[] s = outputBufferTCP.getAll();
			String[] filtereds = NetworkOptimizer.filterObsoleteMessages(s);
			String message = NetworkMessageSerializer.concatenate(filtereds);

			if (message.equals("")) {
				EduLog.warning("Message empty!!");
				return;
			}
			sendUDPMessage(message);
			EduLog.info("Server.networking.sendall");
		} catch (BufferIsEmptyException e) {
			// do nothing if there is no message.
		}
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Stops the sender.
	 */
	public void stopSender() {
		running = false;
	}

	ServerClient getClientById(int id) {
		return clients.get(id);
	}

	/**
	 * Sends an event to all clients.
	 * 
	 * @param event
	 *            The event
	 */
	public void sendEventToAll(Event event) {
		String eventAsString;
		try {
			eventAsString = NetworkMessageSerializer.serialize(event);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}

		if (networkPolicy.determinePacketType(event) == PacketType.TCP) {
			outputBufferTCP.append(eventAsString);
		} else {
			outputBufferUDP.append(eventAsString);
		}
	}

	/**
	 * Sends an event to the specified client.
	 * 
	 * @param event
	 *            The event to send to the client.
	 * @param clientId
	 *            The client's identifier.
	 */
	public void sendEventToClient(Event event, int clientId) {
		String eventAsString;
		try {
			eventAsString = NetworkMessageSerializer.serialize(event);
			PacketType packetType = networkPolicy.determinePacketType(event);
			sendMessageToClient(clientId, eventAsString, packetType);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
	}
}