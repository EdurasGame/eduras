package de.illonis.eduras.events;

import de.illonis.eduras.Statistic.StatsProperty;

/**
 * This event is used to set a statistics value of a player.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetStatsEvent extends GameEvent {

	private final StatsProperty property;
	private final int playerId;
	private final int newCount;

	/**
	 * Create a new SetStatsEvent.
	 * 
	 * @param property
	 *            the stat to set
	 * @param playerId
	 *            the player to set the statistic of
	 * @param newCount
	 *            the new value to set the statistic prop to.
	 */
	public SetStatsEvent(StatsProperty property, int playerId, int newCount) {
		super(GameEventNumber.SET_STATS);
		this.property = property;
		this.playerId = playerId;
		this.newCount = newCount;

		putArgument(property.name());
		putArgument(playerId);
		putArgument(newCount);
	}

	/**
	 * The property this event is about.
	 * 
	 * @return property
	 */
	public StatsProperty getProperty() {
		return property;
	}

	/**
	 * The player who's property to set.
	 * 
	 * @return id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * The new count.
	 * 
	 * @return new count
	 */
	public int getNewCount() {
		return newCount;
	}

}
