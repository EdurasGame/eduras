package de.illonis.eduras.networking;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.illonis.eduras.Logic;
import de.illonis.eduras.events.GameEvent;

/**
 * ServerLogic is used to receive messages from clients and translate them into
 * GameEvents to hand them on to logic.
 * 
 * @author illonis
 * 
 */
public class ServerLogic extends Thread {

	private Buffer inputBuffer;
	private Logic logic;

	/**
	 * Creates a new ServerLogic that pulls messages from given inputbuffer and
	 * parses them to logic.
	 * 
	 * @param inputBuffer
	 *            Buffer to read messages from at specific interval.
	 * @param logic
	 *            Logic to push gameevents into.
	 */
	public ServerLogic(Buffer inputBuffer, Logic logic) {
		this.logic = logic;
		this.inputBuffer = inputBuffer;
	}

	@Override
	public void run() {
		while (true) {
			decodeMessages();
			try {
				Thread.sleep(10);
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
			while (true) {
				String s = inputBuffer.getNext();
				LinkedList<GameEvent> ll = NetworkMessageDeserializer
						.deserialize(s);
				for (GameEvent event : ll)
					logic.onGameEventAppeared(event);
			}
		} catch (NoSuchElementException e) {
			try {
				wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}