package de.illonis.eduras.units;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;

/**
 * A disposable unit is a unit that is removed as soon as it dies.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class DisposableUnit extends Unit {

	private final static Logger L = EduLog.getLoggerFor(DisposableUnit.class
			.getName());

	/**
	 * Create a new disposable unit.
	 * 
	 * @param game
	 * @param timingSource
	 * @param maxHealth
	 * @param id
	 */
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
