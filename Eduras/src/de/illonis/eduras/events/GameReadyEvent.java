package de.illonis.eduras.events;

/**
 * Indicates that game is ready and client can assume valid information.
 * 
 * @author illonis
 * 
 */
public class GameReadyEvent extends NetworkEvent {

	/**
	 * Creates a new GameReadyEvent.
	 */
	public GameReadyEvent() {
		super(NetworkEventNumber.GAME_READY, -1);
	}
}
