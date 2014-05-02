package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class ResurrectPlayerEvent extends RTSActionEvent {

	private final static Logger L = EduLog
			.getLoggerFor(ResurrectPlayerEvent.class.getName());

	private final int idOfPlayerToRespawn;
	private final int idOfBaseToRespawnAt;

	public ResurrectPlayerEvent(int executingPlayer, int playerToRespawn,
			int baseToRespawnAt) {
		super(GameEventNumber.RESURRECT_PLAYER, executingPlayer);

		putArgument(playerToRespawn);
		putArgument(baseToRespawnAt);

		idOfPlayerToRespawn = playerToRespawn;
		idOfBaseToRespawnAt = baseToRespawnAt;
	}

	public int getIdOfPlayerToRespawn() {
		return idOfPlayerToRespawn;
	}

	public int getIdOfBaseToRespawnAt() {
		return idOfBaseToRespawnAt;
	}

}
