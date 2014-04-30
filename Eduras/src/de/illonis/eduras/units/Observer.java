package de.illonis.eduras.units;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

public class Observer extends Unit {

	private final static Logger L = EduLog.getLoggerFor(Observer.class
			.getName());

	public Observer(GameInformation game, TimingSource timingSource, int id,
			int owner) {
		super(game, timingSource, S.unit_observer_maxhealth, id);

		setOwner(owner);
		setVisionAngle(S.unit_observer_visionangle);
		setVisionRange(S.unit_observer_visionrange);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// don't do anything
	}
}
