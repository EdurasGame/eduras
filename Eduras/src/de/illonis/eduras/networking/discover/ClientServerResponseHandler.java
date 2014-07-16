package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.utils.Pair;

/**
 * Handles a server discovery response on client side.
 * 
 * @author illonis
 * 
 */
public class ClientServerResponseHandler extends Thread {

	private final static Logger L = EduLog
			.getLoggerFor(ClientServerResponseHandler.class.getName());

	private final ServerFoundListener listener;
	private final DiscoveryChannel c;

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
				L.info("[ServerSearcher] Got response from "
						+ fsocket.getAddress().getHostAddress());

				String message = returnData.getSecond();

				L.fine("[ServerSearcher] Response is: " + message);

				for (String singleMessage : message.split("##")) {
					parseAndProcessMessage(singleMessage, fsocket);
				}

			}
			c.close();
		} catch (IOException e) {
			L.log(Level.SEVERE, "error finding server", e);
		}
	}

	private void parseAndProcessMessage(String singleMessage,
			InetSocketAddress fsocket) {
		if (singleMessage.contains(ServerDiscoveryListener.ANSWER_MSG)) {
			String[] msgparts = singleMessage.split("#");

			try {
				ServerInfo info = handleServerAnswer(msgparts, fsocket);
				listener.onServerFound(info);
			} catch (NumberFormatException e) {
				L.log(Level.WARNING, "Got corrupted server answer: "
						+ singleMessage, e);
			}

		}
	}

	private ServerInfo handleServerAnswer(String[] msgparts,
			InetSocketAddress fsocket) throws NumberFormatException {
		// handle server
		int port = Integer.parseInt(msgparts[2]);
		int numberOfPlayers = Integer.parseInt(msgparts[4]);
		ServerInfo info = new ServerInfo(msgparts[1], fsocket.getAddress(),
				port, msgparts[3], numberOfPlayers, msgparts[5], msgparts[6]);
		return info;
	}

	@Override
	public void interrupt() {
		c.close();
		super.interrupt();
	}
}
