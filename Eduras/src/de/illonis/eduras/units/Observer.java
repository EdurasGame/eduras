package de.illonis.eduras.units;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.ai.movement.MotionType;
import de.illonis.eduras.ai.movement.MovingUnitAI;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

/**
 * A unit that has vision but no other abilities.
 * 
 * @author illonis
 * 
 */
public class Observer extends ControlledUnit {

	/**
	 * Creates a new observer.
	 * 
	 * @param game
	 *            the gameinfo.
	 * @param timingSource
	 *            timing-source
	 * @param id
	 *            the object id.
	 * @param owner
	 *            the owner of the observer.
	 */
	public Observer(GameInformation game, TimingSource timingSource, int id,
			int owner) {
		super(game, timingSource, S.Server.unit_observer_maxhealth, id);
		setObjectType(ObjectType.OBSERVER);
		ai = new MovingUnitAI(this);
		setOwner(owner);
		setSpeed(S.Server.unit_observer_speed);
		setShape(new Rectangle(0, 0, 15, 15));
		setVisionAngle(S.Server.unit_observer_visionangle);
		setVisionRange(S.Server.unit_observer_visionrange);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// stop all movement
		ai.discard();
	}

	@Override
	public MotionType getMotionType() {
		return MotionType.FOOT;
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		return true;
	}

}
