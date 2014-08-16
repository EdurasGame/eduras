package de.illonis.eduras.gameobjects;

import java.util.LinkedList;
import java.util.SortedSet;
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

	private final SortedSet<GameObject> presentObjects;

	/**
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public TriggerArea(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.TRIGGER_AREA);
		setCollidable(false);
		setzLayer(0);
		presentObjects = new TreeSet<GameObject>(new GameObjectIdComparator());
		if (timingSource != null)
			timingSource.addTimedEventHandler(this);
	}

	/**
	 * @return a set of objects that are currently in this area.
	 */
	public final SortedSet<GameObject> getPresentObjects() {
		return new TreeSet<GameObject>(presentObjects);
	}

	@Override
	public synchronized void onTouch(GameObject collidingObject) {
		synchronized (collidingObject) {
			if (presentObjects.add(collidingObject))
				onObjectEntered(collidingObject);
		}
	};

	@Override
	public void onCollision(GameObject collidingObject) {
		// never called
	}

	@Override
	public final synchronized void onIntervalElapsed(long delta) {
		// System.out.println("[AREA] elapsed");

		LinkedList<GameObject> leavingObjects = new LinkedList<GameObject>();
		for (GameObject obj : presentObjects) {
			// check if the object is still in the game at all
			if (!getGame().getObjects().containsKey(obj.getId())) {
				leavingObjects.add(obj);
			} else {
				synchronized (obj) {
					if (!obj.getShape().intersects(getShape())) {
						leavingObjects.add(obj);
					}
				}
			}
		}

		for (GameObject obj : leavingObjects) {
			presentObjects.remove(obj);
			onObjectLeft(obj);
		}

		intervalElapsed(delta);
	}

	protected void intervalElapsed(long delta) {
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

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		return false;
	}
}
