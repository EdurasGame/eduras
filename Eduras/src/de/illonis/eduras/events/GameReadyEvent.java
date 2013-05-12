package de.illonis.eduras.events;

public class GameReadyEvent extends NetworkEvent {

	/**
	 * 
	 * Creates a new GameReadyEvent.
	 */
	public GameReadyEvent() {
		super(NetworkEventNumber.GAME_READY, -1);
	}
}
