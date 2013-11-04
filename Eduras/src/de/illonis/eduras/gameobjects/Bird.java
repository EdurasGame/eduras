package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ai.UnitAI;
import de.illonis.eduras.ai.movement.MotionAIControllable;
import de.illonis.eduras.ai.movement.MotionType;
import de.illonis.eduras.ai.movement.MovingUnitAI;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.BirdShape;

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
	 * @param id
	 *            object id.
	 */
	public Bird(GameInformation game, int id) {
		super(game, id);
		ai = new MovingUnitAI(this);
		setSpeed(40);
		setShape(new BirdShape());
		setPosition(80, 20);
		setCollidable(false);
	}

	@Override
	public UnitAI getAI() {
		return ai;
	}

	@Override
	public void onMapBoundsReached() {
	}

	@Override
	public void startMovingTo(Vector2D direction) {
		setSpeedVector(direction);
	}

	@Override
	public void stopMoving() {
		setSpeedVector(new Vector2D());
	}

	@Override
	public Vector2D getPosition() {
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
