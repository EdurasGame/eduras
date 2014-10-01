package de.illonis.eduras.math;

import java.util.Collection;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.utils.Pair;

/**
 * Provides methods to compute collision between objects.
 * 
 * @author illonis
 * 
 */
public interface ShapeGeometry {

	public Float UNKNOWN_ANGLE = new Float(-1);

	/**
	 * Checks for touching objects.
	 * 
	 * @param object
	 *            the object to test against.
	 * @return a list of objects touching this object.
	 */
	Collection<GameObject> getTouchingObjects(GameObject object);

	/**
	 * Tries to move a gameobject to a target position and computes the maximum
	 * movement until collision. This method does not actually move the object
	 * but performs all collision computing required.
	 * 
	 * @param object
	 *            the object to move.
	 * @param target
	 *            the target position.
	 * @param touched
	 *            returns a list of objects that are touched on movement and the
	 *            angle between the direction of moving and the touched object.
	 *            If the angle is -1 it is unknown.
	 * @param collided
	 *            returns a list of objects that are collided on movement and
	 *            the respective angle. This will likely contain only the first
	 *            object colliding, as movement stops on first collision.
	 * @return the new position of this object.<br>
	 *         A second call on this method with identical parameters will
	 *         return the same result if environment did not change.
	 */
	Vector2f moveTo(MoveableGameObject object, Vector2f target,
			Collection<Pair<GameObject, Float>> touched,
			Collection<Pair<GameObject, Float>> collided);

	/**
	 * Tries to rotate a gameobject by given angle and computes the maximum
	 * angle the object can be rotated until a collision occurs. This method
	 * does not actually rotate the object but performs all collision computing
	 * required.
	 * 
	 * @param object
	 *            the object that should be rotated.
	 * @param angle
	 *            the angle to rotate counterclockwise in degree.
	 * @param touched
	 *            returns a list of objects that are touched on rotation.
	 * @param collided
	 *            returns a list of objects that are collided on rotation. This
	 *            will likely contain only the first object colliding, as
	 *            rotation stops on first collision.
	 * @return the maximum rotation angle.<br>
	 *         A second call on this method with identical parameters will
	 *         return the same result if environment did not change.
	 */
	float rotateTo(GameObject object, float angle,
			Collection<GameObject> touched, Collection<GameObject> collided);

	/**
	 * Computes lists of all objects that collide and touch a given area.
	 * 
	 * @param shape
	 *            the area to test.
	 * @param touched
	 *            returns a list of objects that touch this area.
	 * @param collided
	 *            returns a list of objects that collide this area.
	 * @param exclude
	 *            excludes this object from checks, pass <i>null</i> to exclude
	 *            nothing.
	 * @return true if any object was found, false otherwise.
	 */
	boolean getObjectsIn(Shape shape, Collection<GameObject> touched,
			Collection<GameObject> collided, GameObject exclude);
}
