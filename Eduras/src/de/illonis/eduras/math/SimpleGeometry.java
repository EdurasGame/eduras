package de.illonis.eduras.math;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.utils.Pair;

/**
 * A very dump and slow implementation of geometry. (fma) I don't even think
 * it's slow, but it's inaccurate! :) (/fma)
 * 
 * @author illonis
 * 
 */
public class SimpleGeometry implements ShapeGeometry {

	private final Map<Integer, GameObject> objects;

	/**
	 * @param objs
	 *            the gameobjects.
	 */
	public SimpleGeometry(Map<Integer, GameObject> objs) {
		this.objects = objs;
	}

	@Override
	public Collection<GameObject> getTouchingObjects(GameObject object) {
		LinkedList<GameObject> touching = new LinkedList<GameObject>();
		Shape s = object.getShape();

		for (Iterator<GameObject> iterator = objects.values().iterator(); iterator
				.hasNext();) {
			GameObject o = iterator.next();
			if (GameObject.canCollideWithEachOther(object, o))
				continue;

			if (Geometry.shapeCollides(s, o.getShape())) {
				touching.add(o);
			}
		}
		return touching;
	}

	@Override
	public Vector2f moveTo(MoveableGameObject object, Vector2f target,
			Collection<Pair<GameObject, Float>> touched,
			Collection<Pair<GameObject, Float>> collided) {
		Shape shape = object.getShape();
		Vector2f diff = new Vector2f(object.getShapeOffsetX(),
				object.getShapeOffsetY());
		Vector2f shapeTarget = target.copy().add(diff);
		Vector2f shapeLocation = new Vector2f(shape.getX(), shape.getY());
		shape.setLocation(shapeTarget);

		LinkedList<GameObject> touchedObjects = new LinkedList<GameObject>();
		LinkedList<GameObject> collidedObjects = new LinkedList<GameObject>();
		getObjectsIn(shape, touchedObjects, collidedObjects, object);

		for (GameObject aTouchedObject : touchedObjects) {
			touched.add(new Pair<GameObject, Float>(aTouchedObject,
					ShapeGeometry.UNKNOWN_ANGLE));
		}

		for (GameObject aCollidedObject : collidedObjects) {
			collided.add(new Pair<GameObject, Float>(aCollidedObject,
					ShapeGeometry.UNKNOWN_ANGLE));
		}

		shape.setLocation(shapeLocation);
		if (collided.isEmpty())
			return target;
		else
			return object.getPositionVector();
	}

	@Override
	public float rotateTo(GameObject object, float angle,
			Collection<GameObject> touched, Collection<GameObject> collided) {
		// TODO implement
		return angle;
	}

	@Override
	public boolean getObjectsIn(Shape shape, Collection<GameObject> touched,
			Collection<GameObject> collided, GameObject exclude) {

		for (Iterator<GameObject> iterator = objects.values().iterator(); iterator
				.hasNext();) {
			GameObject o = iterator.next();
			if (o.equals(exclude))
				continue;
			if (o.getType() == ObjectType.MAPBOUNDS) {
				continue;
			}
			if (Geometry.shapeCollides(shape, o.getShape())) {
				if (o.isCollidable(null))
					collided.add(o);
				else
					touched.add(o);
			}
		}
		return !collided.isEmpty() || !touched.isEmpty();
	}
}
