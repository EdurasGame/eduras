package de.illonis.eduras.items.powerups;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.OneTimeTimedEventHandler;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

public class SpeedPowerUp extends RespawnablePowerUp {

	private final static Logger L = EduLog.getLoggerFor(SpeedPowerUp.class
			.getName());

	public SpeedPowerUp(TimingSource timingSource, GameInformation gi, int id) {
		super(ObjectType.SPEED_POWERUP, timingSource, gi, id);
		setShape(new Circle(0, 0, S.Server.go_speedpowerup_radius));
	}

	@Override
	public void onActivation(final PlayerMainFigure touchingPlayer) {
		final float oldSpeed = touchingPlayer.getSpeed();
		getGame().getEventTriggerer().changeSpeedBy(touchingPlayer,
				S.Server.go_speedpowerup_amount);

		new OneTimeTimedEventHandler(getTimingSource()) {

			@Override
			public long getInterval() {
				return S.Server.go_speedpowerup_duration;
			}

			@Override
			public void intervalElapsed() {
				getGame().getEventTriggerer()
						.setSpeed(touchingPlayer, oldSpeed);
			}
		};
	}

	@Override
	public long getRespawnTime() {
		return S.Server.go_speedpowerup_respawntime;
	}
}
