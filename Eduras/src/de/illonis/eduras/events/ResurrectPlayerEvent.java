package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.actions.RespawnPlayerAction;

/**
 * The respective event for a {@link RespawnPlayerAction}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ResurrectPlayerEvent extends RTSActionEvent {

	private final static Logger L = EduLog
			.getLoggerFor(ResurrectPlayerEvent.class.getName());

	private final int idOfPlayerToRespawn;
	private final int idOfBaseToRespawnAt;

	/**
	 * Create a new event.
	 * 
	 * @param executingPlayer
	 *            (owner)id player who wants to execute the action.
	 * @param playerToRespawn
	 *            (owner)id of player to be respawned
	 * @param baseToRespawnAt
	 *            objectId of base to respawn the player at.
	 */
	public ResurrectPlayerEvent(int executingPlayer, int playerToRespawn,
			int baseToRespawnAt) {
		super(GameEventNumber.RESURRECT_PLAYER, executingPlayer);

		putArgument(playerToRespawn);
		putArgument(baseToRespawnAt);

		idOfPlayerToRespawn = playerToRespawn;
		idOfBaseToRespawnAt = baseToRespawnAt;
	}

	/**
	 * Returns the (owner)id of the player to respawn.
	 * 
	 * @return id
	 */
	public int getIdOfPlayerToRespawn() {
		return idOfPlayerToRespawn;
	}

	/**
	 * Returns the objectid of the base to respawn the player at.
	 * 
	 * @return objectid
	 */
	public int getIdOfBaseToRespawnAt() {
		return idOfBaseToRespawnAt;
	}

}
