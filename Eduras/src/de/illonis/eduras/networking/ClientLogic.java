package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.GameLogicInterface;
import de.illonis.eduras.events.GameEvent;

public class ClientLogic extends Thread {

	GameLogicInterface logic;
	String messages;

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