package de.illonis.eduras.events;

/**
 * This event sets the remaining time of the current round.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetRemainingTimeEvent extends GameEvent {

	private long remainingTime;

	/**
	 * Creates a new SetRemainingTimeEvent.
	 * 
	 * @param remainingTime
	 *            the remaining time in milliseconds.
	 */
	public SetRemainingTimeEvent(long remainingTime) {
		super(GameEventNumber.SET_REMAININGTIME);
		this.remainingTime = remainingTime;
		putArgument(remainingTime);
	}

	/**
	 * Returns the remaining time.
	 * 
	 * @return The remaining time in milliseconds.
	 */
	public long getRemainingTime() {
		return remainingTime;
	}

}
