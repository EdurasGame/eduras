package de.illonis.eduras;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.DataMissingException;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.Building;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.items.weapons.ExampleWeapon;
import de.illonis.eduras.items.weapons.SimpleMissile;
import de.illonis.eduras.items.weapons.SniperMissile;
import de.illonis.eduras.items.weapons.SniperWeapon;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.units.Player;

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
	@SuppressWarnings("javadoc")
	public enum ObjectType {
		PLAYER(1), YELLOWCIRCLE(2), SIMPLEMISSILE(3), ITEM_WEAPON_1(4), NO_OBJECT(
				0), BIGBLOCK(5), SMALLCIRCLEDBLOCK(6), SNIPERMISSILE(7), ITEM_WEAPON_SNIPER(
				8), BUILDING(9);

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

	/**
	 * Handles an object attribute changed event.
	 * 
	 * @param event
	 *            {@link SetGameObjectAttributeEvent} that occured.
	 */
	public void onObjectAttributeChanged(SetGameObjectAttributeEvent<?> event) {

		for (GameEventListener listener : logic.getListenerList()) {
			listener.onObjectStateChanged(event);
		}

	}

	/**
	 * Handles an object factory event. That means it creates a new gameobject
	 * of given type. An id has to be assigned to objectfactory event.
	 * 
	 * @param event
	 *            object factory event with object id attached.
	 */
	public void onObjectFactoryEventAppeared(ObjectFactoryEvent event) {

		GameObject go = null;
		if (event.getType() == GameEventNumber.OBJECT_CREATE) {

			// skip creating object that already exist
			if (logic.getGame().getObjects().containsKey(event.getId())) {
				return;
			}

			int owner = event.getOwner();
			int id;
			if (event.hasId()) {
				id = event.getId();
			} else {
				EduLog.passException(new DataMissingException("No id assigned!"));
				return;
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
			case BUILDING:
				go = new Building(logic.getGame(), id);
				break;
			case ITEM_WEAPON_1:
				go = new ExampleWeapon(logic.getGame(), id);
				break;
			case BIGBLOCK:
				try {
					go = new BigBlock(logic.getGame(), id);
				} catch (ShapeVerticesNotApplicableException e) {
					EduLog.passException(e);
					return;
				}
				break;
			case SNIPERMISSILE:
				go = new SniperMissile(logic.getGame(), id);
				break;
			case ITEM_WEAPON_SNIPER:
				go = new SniperWeapon(logic.getGame(), id);
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

			GameObject objectToRemove = logic.getGame().getObjects().get(id);
			logic.getGame().removeObject(objectToRemove);

			for (GameEventListener gel : logic.getListenerList()) {
				gel.onObjectRemove(event);
			}
		}
	}
}