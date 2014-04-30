package de.illonis.eduras.units;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;

public abstract class DisposableUnit extends Unit {

	private final static Logger L = EduLog.getLoggerFor(DisposableUnit.class
			.getName());

	public DisposableUnit(GameInformation game, TimingSource timingSource,
			int maxHealth, int id) {
		super(game, timingSource, maxHealth, id);
	}

	@Override
	protected final void onDead(int killer) {
		getGame().getEventTriggerer().removeObject(this.getId());
	}

	@Override
	public abstract void onCollision(GameObject collidingObject);
}
