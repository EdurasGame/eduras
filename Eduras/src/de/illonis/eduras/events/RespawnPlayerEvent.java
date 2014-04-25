package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class RespawnPlayerEvent extends GameEvent {

	private final static Logger L = EduLog
			.getLoggerFor(RespawnPlayerEvent.class.getName());

	private final int idOfExecutingPlayer;
	private final int idOfPlayerToRespawn;
	private final int idOfBaseToRespawnAt;

	public RespawnPlayerEvent(int executingPlayer, int playerToRespawn,
			int baseToRespawnAt) {
		super(GameEventNumber.RESPAWN_PLAYER);

		putArgument(executingPlayer);
		putArgument(playerToRespawn);
		putArgument(baseToRespawnAt);

		idOfExecutingPlayer = executingPlayer;
		idOfPlayerToRespawn = playerToRespawn;
		idOfBaseToRespawnAt = baseToRespawnAt;
	}

	public int getIdOfExecutingPlayer() {
		return idOfExecutingPlayer;
	}

	public int getIdOfPlayerToRespawn() {
		return idOfPlayerToRespawn;
	}

	public int getIdOfBaseToRespawnAt() {
		return idOfBaseToRespawnAt;
	}

}
