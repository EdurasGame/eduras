package de.illonis.eduras.events;

/**
 * This event represents a notification from the client to the server that the
 * player wants to perform an action.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RTSActionEvent extends GameEvent {

	private int executingPlayer;

	/**
	 * Create an RTSActionEvent.
	 * 
	 * @param type
	 *            event number
	 * @param executingPlayer
	 *            the player who wants to perform the action
	 */
	public RTSActionEvent(GameEventNumber type, int executingPlayer) {
		super(type);

		this.executingPlayer = executingPlayer;
		putArgument(executingPlayer);
	}

	/**
	 * Returns the player who wants to perform the action.
	 * 
	 * @return player
	 */
	public int getExecutingPlayer() {
		return executingPlayer;
	}

}
