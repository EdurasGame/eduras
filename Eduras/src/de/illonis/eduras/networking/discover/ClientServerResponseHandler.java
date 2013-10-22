package de.illonis.eduras.networking.discover;

import java.io.IOException;
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
	 * @param c
	 */
	public ClientServerResponseHandler(ServerFoundListener listener,
			DiscoveryChannel c) {
		super("ClientServerResponseHandler");
		this.c = c;
		this.listener = listener;
	}

	@Override
	public void run() {
		try {

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
