package de.illonis.eduras.units;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ai.UnitAI;
import de.illonis.eduras.ai.movement.MotionAIControllable;
import de.illonis.eduras.gameobjects.TimingSource;

/**
 * A user controlled unit.
 * 
 * @author illonis
 * 
 */
public abstract class ControlledUnit extends DisposableUnit implements
		MotionAIControllable {

	protected UnitAI ai;

	/**
	 * Creates a new controlled unit. Make sure you set {@link #ai} to prevent
	 * errors.
	 * 
	 * @param game
	 *            gameinfo.
	 * @param timingSource
	 *            timingsource
	 * @param maxHealth
	 *            unit health.
	 * @param id
	 *            unit id.
	 */
	public ControlledUnit(GameInformation game, TimingSource timingSource,
			int maxHealth, int id) {
		super(game, timingSource, maxHealth, id);
	}

	@Override
	public UnitAI getAI() {
		return ai;
	}

	@Override
	public void stopMoving() {
		setSpeedVector(new Vector2f());
	}

	@Override
	public void startMovingTo(Vector2f direction) {
		setSpeedVector(direction);
	}

	@Override
	public Vector2f getPosition() {
		return getPositionVector();
	}

}
