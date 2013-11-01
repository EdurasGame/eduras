package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.utils.Pair;

/**
 * A {@link DatagramChannel} that is prepared for server discovery and handles
 * all the crappy buffer things to do.
 * 
 * @author illonis
 * 
 */
public class DiscoveryChannel {

	private final static Logger L = EduLog.getLoggerFor(DiscoveryChannel.class
			.getName());

	/**
	 * Size of buffer.
	 */
	public final static int BUFFER_SIZE = 15000;

	private final DatagramChannel channel;
	private final ByteBuffer readBuffer, writeBuffer;

	/**
	 * Creates a new channel.
	 * 
	 * @param blocking
	 *            set this to true if channel operations should be blocking.
	 * @throws IOException
	 *             if initialization failed.
	 */
	public DiscoveryChannel(boolean blocking) throws IOException {
		channel = DatagramChannel.open();
		channel.setOption(StandardSocketOptions.SO_SNDBUF, BUFFER_SIZE);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, BUFFER_SIZE);
		channel.setOption(StandardSocketOptions.SO_BROADCAST, true);
		channel.configureBlocking(blocking);
		readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	}

	/**
	 * Binds this channel to given local address.
	 * 
	 * @param addr
	 *            target address.
	 * @throws IOException
	 *             if binding failed.
	 * @see DatagramChannel#bind(SocketAddress)
	 * 
	 * @author illonis
	 */
	public void bind(SocketAddress addr) throws IOException {
		channel.bind(addr);
	}

	/**
	 * Sends given message to given target.
	 * 
	 * @param message
	 *            the message.
	 * @param target
	 *            the target.
	 * 
	 * @author illonis
	 * @throws IOException
	 *             if I/O error occurs.
	 */
	public void send(String message, SocketAddress target) throws IOException {
		writeBuffer.clear();
		writeBuffer.put(message.getBytes());
		writeBuffer.flip();
		channel.send(writeBuffer, target);
	}

	/**
	 * Receives data from channel.
	 * 
	 * @return a pair containing address of sender and message or null if
	 *         nothing to receive.
	 * 
	 * @author illonis
	 * @throws IOException
	 *             if I/O error occurs.
	 */
	public Pair<SocketAddress, String> receive() throws IOException {
		readBuffer.clear();
		SocketAddress addr;
		try {
			addr = channel.receive(readBuffer);
		} catch (IOException e) {
			close();
			return null;
		}
		if (addr == null)
			return null;

		byte[] arr = new byte[BUFFER_SIZE];
		readBuffer.position(0);
		readBuffer.get(arr);
		String message = new String(arr).trim();

		return new Pair<SocketAddress, String>(addr, message);

	}

	/**
	 * Closes this channel.
	 * 
	 * @author illonis
	 */
	public void close() {
		try {
			channel.close();
		} catch (IOException e) {
			L.log(Level.WARNING, "channel closed", e);
		}
	}

}