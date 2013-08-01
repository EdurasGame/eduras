package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import de.illonis.eduras.logger.EduLog;

/**
 * Searches for servers in local network using UDP broadcasts.
 * 
 * @author illonis
 * 
 */
public class ServerSearcher extends Thread {
	private DatagramSocket c;
	private ServerFoundListener listener;

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

	@Override
	public void run() {
		if (listener == null) {
			throw new IllegalStateException(
					"There is no listener attached to ServerSearcher.");
		}
		// Find the server using UDP broadcast
		try {
			// Open a random port to send the package
			c = new DatagramSocket();
			c.setBroadcast(true);

			byte[] sendData = ServerDiscoveryListener.REQUEST_MSG.getBytes();

			// Try the 255.255.255.255 first
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length,
						InetAddress.getByName("255.255.255.255"),
						ServerDiscoveryListener.DISCOVERY_PORT);
				c.send(sendPacket);
				System.out
						.println(getClass().getName()
								+ ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
			} catch (Exception e) {
			}

			// Broadcast the message over all the network interfaces
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue; // Don't want to broadcast to the loopback
								// interface
				}

				for (InterfaceAddress interfaceAddress : networkInterface
						.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					// Send the broadcast package!
					try {
						DatagramPacket sendPacket = new DatagramPacket(
								sendData, sendData.length, broadcast,
								ServerDiscoveryListener.DISCOVERY_PORT);
						c.send(sendPacket);
					} catch (Exception e) {
					}

					System.out.println(getClass().getName()
							+ ">>> Request packet sent to: "
							+ broadcast.getHostAddress() + "; Interface: "
							+ networkInterface.getDisplayName());
				}
			}

			System.out
					.println(getClass().getName()
							+ ">>> Done looping over all network interfaces. Now waiting for a reply!");

			// Wait for a response
			byte[] recvBuf = new byte[15000];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf,
					recvBuf.length);
			c.receive(receivePacket);

			// We have a response
			System.out.println(getClass().getName()
					+ ">>> Broadcast response from server: "
					+ receivePacket.getAddress().getHostAddress());

			// Check if the message is correct
			String message = new String(receivePacket.getData()).trim();
			if (message.contains(ServerDiscoveryListener.ANSWER_MSG)) {
				String[] msgparts = message.split("#");

				// DO SOMETHING WITH THE SERVER'S IP (for example, store it in
				// your controller)
				int port = 0;
				try {
					port = Integer.parseInt(msgparts[2]);
					ServerInfo info = new ServerInfo(msgparts[1],
							receivePacket.getAddress(), port);
					listener.onServerFound(info);
				} catch (NumberFormatException ne) {
				}

			}

			// Close the port!
			c.close();
		} catch (IOException ex) {
			EduLog.passException(ex);
		}
	}

	@Override
	public void interrupt() {
		c.close();
		super.interrupt();
	}

}