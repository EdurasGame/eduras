package de.illonis.eduras.items.powerups;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

public class InvisibilityPowerUp extends RespawnablePowerUp {

	private final static Logger L = EduLog
			.getLoggerFor(InvisibilityPowerUp.class.getName());

	public InvisibilityPowerUp(TimingSource timingSource, GameInformation gi,
			int id) {
		super(ObjectType.INVISIBILITY_POWERUP, timingSource, gi, id);
		setShape(new Circle(0, 0, S.Server.go_invisibility_powerup_radius));
	}

	@Override
	public void onActivation(final PlayerMainFigure touchingPlayer) {
		getGame().getEventTriggerer().makeInvisibleForSomeTime(touchingPlayer,
				S.Server.go_invisibility_powerup_duration);

	}

	@Override
	public long getRespawnTime() {
		return S.Server.go_invisibility_powerup_respawntime;
	}
}
