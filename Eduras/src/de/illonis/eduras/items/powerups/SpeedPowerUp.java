package de.illonis.eduras.items.powerups;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
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

		getGame().getEventTriggerer().speedUpObjectForSomeTime(touchingPlayer,
				S.Server.go_speedpowerup_duration,
				S.Server.go_speedpowerup_amount);
	}

	@Override
	public long getRespawnTime() {
		return S.Server.go_speedpowerup_respawntime;
	}
}
