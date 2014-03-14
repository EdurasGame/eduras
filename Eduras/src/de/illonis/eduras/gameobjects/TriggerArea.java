package de.illonis.eduras.gameobjects;

import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * An area that tracks gameobjects in it and notifies when an object entered or
 * left the area.
 * 
 * @author illonis
 * 
 */
public abstract class TriggerArea extends GameObject implements
		TimedEventHandler {

	private final static Logger L = EduLog.getLoggerFor(TriggerArea.class
			.getName());

	/**
	 * Interval for checking for leaving objects in ms.
	 */
	private final static long CHECK_INTERVAL = 100;

	private final Set<GameObject> presentObjects;

	/**
	 * @param game
	 *            game information.
	 * @param id
	 *            object id.
	 */
	public TriggerArea(GameInformation game, int id) {
		super(game, id);
		setObjectType(ObjectType.TRIGGER_AREA);
		presentObjects = new TreeSet<GameObject>(new GameObjectIdComparator());
	}

	/**
	 * @return a set of objects that are currently in this area.
	 */
	public final Set<GameObject> getPresentObjects() {
		return new TreeSet<GameObject>(presentObjects);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		presentObjects.add(collidingObject);
		onObjectEntered(collidingObject);
	};

	@Override
	public void onIntervalElapsed(long delta) {
		Rectangle2D.Double thisBounds = getBoundingBox();
		for (GameObject obj : presentObjects) {
			if (!obj.getBoundingBox().intersects(thisBounds)) {
				presentObjects.remove(obj);
				onObjectLeft(obj);
			}
		}
	}

	@Override
	public long getInterval() {
		return CHECK_INTERVAL;
	}

	/**
	 * Triggered when an object enters this area.
	 * 
	 * @param object
	 *            the object that entered area.
	 */
	public abstract void onObjectEntered(GameObject object);

	/**
	 * Triggered when an object leaves this area.
	 * 
	 * @param object
	 *            the object that left area.
	 */
	public abstract void onObjectLeft(GameObject object);
}
