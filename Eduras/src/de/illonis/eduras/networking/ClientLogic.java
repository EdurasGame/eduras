package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.interfaces.GameLogicInterface;

/**
 * Processes messages that arrive at the client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 *         More detailed, the incoming message is deserialized and forwarded to
 *         the gamelogic
 */
public class ClientLogic extends Thread {

	GameLogicInterface logic;
	String messages;

	/**
	 * Creates a new ClientLogic that deserializes the given message into an
	 * event and forwards it to the given GameLogic.
	 * 
	 * @param logic
	 *            The GameLogic the event is forwarded to.
	 * @param messages
	 *            The message that is deserialized into an event.
	 */
	public ClientLogic(GameLogicInterface logic, String messages) {
		this.logic = logic;
		this.messages = messages;
	}

	@Override
	public void run() {

		LinkedList<GameEvent> eventList = NetworkMessageDeserializer
				.deserialize(messages);

		for (GameEvent event : eventList) {
			logic.onGameEventAppeared(event);
		}
	}
}