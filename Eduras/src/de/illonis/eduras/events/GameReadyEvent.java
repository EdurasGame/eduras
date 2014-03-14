package de.illonis.eduras.events;

/**
 * Indicates that game is ready and client can assume valid information.
 * 
 * @author illonis
 * 
 */
public class GameReadyEvent extends GameEvent {

	/**
	 * Creates a new GameReadyEvent.
	 */
	public GameReadyEvent() {
		super(GameEventNumber.GAME_READY);
	}
}
