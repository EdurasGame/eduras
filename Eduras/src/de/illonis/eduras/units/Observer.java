package de.illonis.eduras.units;

import org.newdawn.slick.geom.Circle;

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
		setShape(new Circle(0, 0, S.Server.unit_observer_shaperadius));
		setVisionAngle(S.Server.unit_observer_visionangle);
		setVisionRange(S.Server.unit_observer_visionrange);
		setDetector(true);
		setDetectionRange(S.Server.unit_observer_detectionrange);

		if (S.Server.unit_observer_stealth) {
			setVisible(Visibility.OWNER_TEAM);
		}
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
	}

	@Override
	public MotionType getMotionType() {
		return MotionType.FOOT;
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		if (S.Server.unit_observer_stealth) {
			return !otherObject.isUnit();
		} else {
			return true;
		}
	}
}
