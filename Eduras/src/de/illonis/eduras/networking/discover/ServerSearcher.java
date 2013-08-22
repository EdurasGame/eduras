package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
	private DiscoveryChannel c;
	private ClientServerResponseHandler handler;
	private ServerFoundListener listener;
	/**
	 * Broadcast interval in milliseconds.
	 */
	public final static int BROADCAST_INTERVAL = 2000;

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
			EduLog.info("[ServerSearcher] Sent request packet via 255.255.255.255.");
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

					c.send(ServerDiscoveryListener.REQUEST_MSG, target);
					EduLog.info("[ServerSearcher] Sent request packet to "
							+ broadcast.getHostAddress() + " via "
							+ networkInterface.getDisplayName() + ".");

				}
			}
		} catch (IOException e) {
			EduLog.passException(e);
		}
	}

	private boolean init() {
		try {
			// Open a random port to send the package
			c = new DiscoveryChannel(false);
		} catch (IOException e) {
			EduLog.passException(e);
			return false;
		}

		handler = new ClientServerResponseHandler(listener);
		handler.start();

		try {
			synchronized (handler) {
				// wait for response handler to be ready.
				// this is required so we do not receive anything before we
				// listen to it.
				handler.wait();
			}
		} catch (InterruptedException e) {
			EduLog.passException(e);
			return false;
		}
		return true;
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
			try {
				Thread.sleep(BROADCAST_INTERVAL);
			} catch (InterruptedException e) {
				break;
			}
		}

		c.close();
	}

	@Override
	public void interrupt() {
		c.close();
		handler.interrupt();
		super.interrupt();
	}

}
