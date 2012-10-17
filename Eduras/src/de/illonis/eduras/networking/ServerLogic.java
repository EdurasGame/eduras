package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.GameLogicInterface;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.exceptions.BufferIsEmptyException;

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
		while (true) {
			decodeMessages();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Decodes all messages that are in inputBuffer. Once there is no message
	 * available anymore, thread goes to bed.
	 */
	private void decodeMessages() {
		try {
			String s = inputBuffer.getNext();
			LinkedList<GameEvent> deserializedMessages = NetworkMessageDeserializer
					.deserialize(s);
			System.out.println("[SERVERLOGIC] Decoded "
					+ deserializedMessages.size() + " messages from: " + s);
			for (GameEvent event : deserializedMessages)
				logic.onGameEventAppeared(event);

		} catch (BufferIsEmptyException e) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}