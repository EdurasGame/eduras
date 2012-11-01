package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;

/**
 * Processes messages that arrive at the client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 *         More detailed, the incoming message is deserialized and forwarded to
 *         the gamelogic or to the network listener.
 */
public class ClientLogic extends Thread {

	NetworkEventListener networkEventListener;
	GameLogicInterface logic;
	String messages;

	/**
	 * Creates a new ClientLogic that deserializes the given message into an
	 * event and forwards it to the given GameLogic or to the given
	 * NetworkEventListener.
	 * 
	 * @param logic
	 *            The GameLogic the event is forwarded to.
	 * @param messages
	 *            The message that is deserialized into an event.
	 * @param networkEventListener
	 *            The network event listener to forward NetworkEvents to.
	 */
	public ClientLogic(GameLogicInterface logic, String messages,
			NetworkEventListener networkEventListener) {
		this.logic = logic;
		this.messages = messages;
		this.networkEventListener = networkEventListener;
	}

	@Override
	public void run() {

		LinkedList<Event> eventList = NetworkMessageDeserializer
				.deserialize(messages);

		for (Event event : eventList) {
			if (event instanceof GameEvent) {
				logic.onGameEventAppeared((GameEvent) event);
			} else {
				networkEventListener
						.onNetworkEventAppeared((NetworkEvent) event);
			}
		}
	}
}