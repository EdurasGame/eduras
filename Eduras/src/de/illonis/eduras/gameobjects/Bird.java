package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ai.UnitAI;
import de.illonis.eduras.ai.movement.MotionAIControllable;
import de.illonis.eduras.ai.movement.MotionType;
import de.illonis.eduras.ai.movement.MovingUnitAI;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ShapeFactory;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

/**
 * A simple flying bird.
 * 
 * @author illonis
 * 
 */
public class Bird extends MoveableGameObject implements MotionAIControllable {

	private final MovingUnitAI ai;

	/**
	 * Creates a new bird.
	 * 
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public Bird(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		ai = new MovingUnitAI(this);
		setSpeed(S.go_bird_speed);
		setShape(ShapeFactory.createShape(ShapeType.BIRD));
		setPosition(80, 20);
		setCollidable(S.go_bird_collidable);
	}

	@Override
	public UnitAI getAI() {
		return ai;
	}
	
	@Override
	public void startMovingTo(Vector2df direction) {
		setSpeedVector(direction);
	}

	@Override
	public void stopMoving() {
		setSpeedVector(new Vector2df());
	}

	@Override
	public Vector2df getPosition() {
		return getPositionVector();
	}

	@Override
	public MotionType getMotionType() {
		return MotionType.RANDOM;
	}

	@Override
	public void onCollision(GameObject collidingObject) {
	}

}
