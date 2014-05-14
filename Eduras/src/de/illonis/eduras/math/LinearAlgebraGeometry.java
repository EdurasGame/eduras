package de.illonis.eduras.math;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;

/**
 * This geometry implementation identifies the cases where
 * {@link SimpleGeometry} isn't accurate enough and tries to handle them better.
 * 
 * @author Florian 'Ren' Mai
 * 
 */
public class LinearAlgebraGeometry extends SimpleGeometry {

	private final static Logger L = EduLog
			.getLoggerFor(LinearAlgebraGeometry.class.getName());

	/**
	 * Create the geometry.
	 * 
	 * @param gameObjects
	 *            The game objects to be taken into account for collision
	 *            detection.
	 */
	public LinearAlgebraGeometry(Map<Integer, GameObject> gameObjects) {
		super(gameObjects);
	}

	@Override
	public Vector2f moveTo(MoveableGameObject object, Vector2f target,
			Collection<GameObject> touched, Collection<GameObject> collided) {

		float distanceToTarget = object.getPositionVector().distance(target);
		if (distanceToTarget < object.getShape().getBoundingCircleRadius()) {
			// in this case, the approach used by SimpleGeometry will always
			// give an exact result, because if it didn't detect a collision
			// with an object by shape intersection, it must also have not been
			// detected in the preceding iteration, which cannot be the case
			return super.moveTo(object, target, touched, collided);
		} else {
			System.out.println("Here we need to calculate this differently.");
			return super.moveTo(object, target, touched, collided);
		}
	}
}
