package de.illonis.eduras.networking;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;

/**
 * {@link ServerDecoder} is used to handle received messages from clients that
 * wait in a input buffer and translate them into GameEvents to hand them on to
 * logic.
 * 
 * @author illonis
 * 
 */
public class ServerDecoder extends Thread {

	private final static Logger L = EduLog.getLoggerFor(ServerDecoder.class
			.getName());

	private final Buffer inputBuffer;
	private final GameLogicInterface logic;
	private final NetworkEventListener networkEventListener;

	/**
	 * Creates a new {@link ServerDecoder} that pulls messages from given
	 * inputbuffer and parses them to logic or networkEventListener respectivly.
	 * 
	 * @param inputBuffer
	 *            Buffer to read messages from at specific interval.
	 * @param logic
	 *            Logic to push gameevents into.
	 * @param networkEventListener
	 *            The listener to forward networkEvents to.
	 */
	public ServerDecoder(Buffer inputBuffer, GameLogicInterface logic,
			NetworkEventListener networkEventListener) {
		setName("ServerDecoder");
		this.logic = logic;
		this.inputBuffer = inputBuffer;
		this.networkEventListener = networkEventListener;
	}

	@Override
	public void run() {
		L.info("[ServerDecoder] Started serverlogic.");
		readFromInputBuffer();
	}

	/**
	 * Reads repeatedly from input buffer and decodes those messages.<br>
	 * This does not need a wait implementation because reading from input
	 * buffer is blocking.
	 */
	private void readFromInputBuffer() {
		while (true) {
			try {
				String s = inputBuffer.getNext();
				decodeMessage(s);
			} catch (InterruptedException e) {
				L.log(Level.WARNING, "decoder interrupted", e);
				break;
			}
		}
	}

	/**
	 * Decodes given messages into one or more events and notices logic.
	 * 
	 * @param message
	 *            message to decode.
	 */
	private void decodeMessage(String message) {
		LinkedList<Event> deserializedMessages = NetworkMessageDeserializer
				.deserialize(message);
		L.info("[ServerDecoder] Decoded " + deserializedMessages.size()
				+ " messages from: " + message);
		for (Event event : deserializedMessages)
			if (event instanceof GameEvent) {
				logic.onGameEventAppeared((GameEvent) event);
			} else {
				networkEventListener
						.onNetworkEventAppeared((NetworkEvent) event);
			}
	}
}