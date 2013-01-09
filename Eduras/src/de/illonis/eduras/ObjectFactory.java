package de.illonis.eduras;

import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.DataMissingException;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.items.weapons.ExampleWeapon;
import de.illonis.eduras.items.weapons.SimpleMissile;
import de.illonis.eduras.logger.EduLog;

/**
 * ObjectFactory is in charge of handling Objectfactory events and creating and
 * removing of objects.
 * 
 * @author illonis
 * 
 */
public class ObjectFactory {

	private final GameLogicInterface logic;

	/**
	 * Collection of object types that can be created by {@link ObjectFactory}.
	 * 
	 * @author illonis
	 * 
	 */
	public enum ObjectType {
		PLAYER(1), YELLOWCIRCLE(2), SIMPLEMISSILE(3), ITEM_WEAPON_1(4), NO_OBJECT(
				0), BLOCK(5), CIRCLEDBLOCK(6);

		private int number;

		ObjectType(int num) {
			number = num;
		}

		public int getNumber() {
			return number;
		}

		public static ObjectType getObjectTypeByNumber(int num) {
			for (ObjectType objectType : ObjectType.values()) {
				if (num == objectType.getNumber()) {
					return objectType;
				}
			}
			return ObjectType.NO_OBJECT;
		}
	}

	/**
	 * Creates a new objectfactory for given game.
	 * 
	 * @param logic
	 *            game object factory is assigned to.
	 */
	public ObjectFactory(GameLogicInterface logic) {
		this.logic = logic;
	}

	public void onObjectAttributeChanged(
			SetBooleanGameObjectAttributeEvent event) {

		for (GameEventListener listener : logic.getListenerList()) {
			listener.onObjectStateChanged(event);
		}

	}

	/**
	 * Handles an object factory event.
	 * 
	 * @param event
	 *            object factory event.
	 */
	public void onObjectFactoryEventAppeared(ObjectFactoryEvent event) {

		// if (ofe.hasId()
		// && logic.getGame().getObjects().containsKey(ofe.getId())) {
		// EduLog.info("Object with id " + ofe.getId()
		// + " already exists.");
		// return;
		// }
		GameObject go = null;
		if (event.getType() == GameEventNumber.OBJECT_CREATE) {

			// skip creating object that already exist
			if (logic.getGame().getObjects().containsKey(event.getId())) {
				return;
			}

			int owner = event.getOwner();
			int id;
			// TODO: Find a more suitable catch clause if the id is missing.
			if (event.hasId()) {
				id = event.getId();
			} else {
				try {
					throw new DataMissingException("No id assigned!");
				} catch (DataMissingException e) {
					e.printStackTrace();
					return;
				}
			}

			switch (event.getObjectType()) {
			case PLAYER:
				go = new Player(logic.getGame(), event.getOwner(), id);
				logic.getGame().addPlayer((Player) go);

				EduLog.info("Player " + event.getOwner() + " created");
				break;
			case SIMPLEMISSILE:
				go = new SimpleMissile(logic.getGame(), id);
				break;
			case ITEM_WEAPON_1:
				go = new ExampleWeapon(logic.getGame(), id);
				break;
			default:
				return;
			}

			go.setId(id);
			go.setOwner(owner);

			if (go != null)
				logic.getGame().addObject(go);

			for (GameEventListener gel : logic.getListenerList()) {
				gel.onObjectCreation(event);
			}
			// game.addObject(go);
		}

		else if (event.getType() == GameEventNumber.OBJECT_REMOVE) {
			int id = event.getId();
			ConcurrentHashMap<Integer, GameObject> gameObjects = logic
					.getGame().getObjects();

			GameObject objectToRemove = gameObjects.get(id);
			logic.getGame().removeObject(objectToRemove);

			for (GameEventListener gel : logic.getListenerList()) {
				gel.onObjectRemove(event);
			}

		}

	}
}
