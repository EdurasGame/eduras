package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.GameLogicInterface;
import de.illonis.eduras.events.GameEvent;

/**
 * ServerLogic is used to receive messages from clients and translate them into
 * GameEvents to hand them on to logic.
 * 
 * @author illonis
 * 
 */
public class ServerLogic extends Thread {

	private final Buffer inputBuffer;
	private final GameLogicInterface logic;

	/**
	 * Creates a new ServerLogic that pulls messages from given inputbuffer and
	 * parses them to logic.
	 * 
	 * @param inputBuffer
	 *            Buffer to read messages from at specific interval.
	 * @param logic
	 *            Logic to push gameevents into.
	 */
	public ServerLogic(Buffer inputBuffer, GameLogicInterface logic) {
		this.logic = logic;
		this.inputBuffer = inputBuffer;
	}

	@Override
	public void run() {
		System.out.println("[SERVERLOGIC] Started serverlogic.");
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
				e.printStackTrace();
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
		LinkedList<GameEvent> deserializedMessages = NetworkMessageDeserializer
				.deserialize(message);
		System.out.println("[SERVERLOGIC] Decoded "
				+ deserializedMessages.size() + " messages from: " + message);
		for (GameEvent event : deserializedMessages)
			logic.onGameEventAppeared(event);
	}
}