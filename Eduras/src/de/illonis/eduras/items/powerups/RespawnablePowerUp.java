package de.illonis.eduras.items.powerups;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.OneTimeTimedEventHandler;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.PowerUpItem;
import de.illonis.eduras.items.Respawnable;
import de.illonis.eduras.settings.S;

public abstract class RespawnablePowerUp extends PowerUpItem implements
		Respawnable {

	private final static Logger L = EduLog
			.getLoggerFor(RespawnablePowerUp.class.getName());

	private long respawnTime;

	/**
	 * Create a new {@link RespawnablePowerUp}.
	 * 
	 * @param type
	 * @param timingSource
	 * @param gi
	 * @param id
	 */
	public RespawnablePowerUp(ObjectType type, TimingSource timingSource,
			GameInformation gi, int id) {
		super(type, timingSource, gi, id);
		respawnTime = S.Server.go_powerup_respawntime_default;
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
		super.onCollision(collidingObject, angle);

		// set up respawntimer
		new RespawnItemTimer(getTimingSource());
	}

	private class RespawnItemTimer extends OneTimeTimedEventHandler {

		public RespawnItemTimer(TimingSource timingSource) {
			super(timingSource);
		}

		@Override
		public void intervalElapsed() {
			getGame().getEventTriggerer().createObjectAt(getType(),
					getPositionVector(), getOwner());
		}

		@Override
		public long getInterval() {
			return getRespawnTime();
		}

	}

	@Override
	public long getRespawnTime() {
		return S.Server.go_powerup_respawntime_default;
	}

	@Override
	public long getRespawnTimeRemaining() {
		return respawnTime;
	}

	@Override
	public boolean reduceRespawnRemaining(long value) {
		if (!getGame().getGameSettings().getGameMode().doItemsRespawn()) {
			return false;
		}

		if (respawnTime == 0)
			return false;
		respawnTime = Math.max(0, respawnTime - value);
		if (respawnTime == 0) {
			return true;
		}
		return false;
	}

}
