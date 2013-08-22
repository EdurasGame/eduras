package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.utils.Pair;

/**
 * Handles a server discovery response on client side.
 * 
 * @author illonis
 * 
 */
public class ClientServerResponseHandler extends Thread {

	private final ServerFoundListener listener;
	private DiscoveryChannel c;

	/**
	 * Creates a new handler.
	 * 
	 * @param listener
	 */
	public ClientServerResponseHandler(ServerFoundListener listener) {
		super("ClientServerResponseHandler");
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			c = new DiscoveryChannel(false);

			try {
				c.bind(new InetSocketAddress(
						ServerDiscoveryListener.CLIENT_PORT));
			} catch (BindException e) {
				EduLog.passException(e);
				listener.onDiscoveryFailed();
				c.close();
				return;
			}

			Pair<SocketAddress, String> returnData = null;
			int i = 0;
			while (!interrupted()) {
				returnData = c.receive();
				if (i == 0) {
					i = 1;
					synchronized (this) {
						// notify ServerSearcher
						this.notify();
					}
				}
				if (returnData == null)
					continue;

				InetSocketAddress fsocket = (InetSocketAddress) returnData
						.getFirst();
				EduLog.info("[ServerSearcher] Got response from "
						+ fsocket.getAddress().getHostAddress());

				String message = returnData.getSecond();

				if (message.contains(ServerDiscoveryListener.ANSWER_MSG)) {
					String[] msgparts = message.split("#");

					// handle server
					try {
						int port = Integer.parseInt(msgparts[2]);
						ServerInfo info = new ServerInfo(msgparts[1],
								fsocket.getAddress(), port);
						listener.onServerFound(info);
					} catch (NumberFormatException ne) {
					}
				}
			}
		} catch (IOException e) {
			EduLog.passException(e);
		}
	}

	@Override
	public void interrupt() {
		c.close();
		super.interrupt();
	}
}
