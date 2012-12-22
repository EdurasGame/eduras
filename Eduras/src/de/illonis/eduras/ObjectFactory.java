package de.illonis.eduras;

import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.test.YellowCircle;

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
				0);

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

	public void onObjectAttributeChanged(SetGameObjectAttributeEvent event) {

		for (GameEventListener listener : logic.getListenerList()) {
			listener.onObjectStateChanged(event);
		}

	}

	public void onGameEventAppeared(GameEvent event) {
		// do not handle other events in case they are received.
		if (!(event instanceof ObjectFactoryEvent))
			return;

		ObjectFactoryEvent ofe = (ObjectFactoryEvent) event;
		// if (ofe.hasId()
		// && logic.getGame().getObjects().containsKey(ofe.getId())) {
		// EduLog.info("Object with id " + ofe.getId()
		// + " already exists.");
		// return;
		// }
		GameObject go = null;
		if (ofe.getType() == GameEventNumber.OBJECT_CREATE) {

			// skip creating object that already exist
			if (logic.getGame().getObjects().containsKey(ofe.getId())) {
				return;
			}

			switch (ofe.getObjectType()) {
			case PLAYER:
				go = new Player(logic.getGame(), ofe.getOwner());
				go.setOwner(ofe.getOwner());
				if (ofe.hasId())
					go.setId(ofe.getId());
				else
					ofe.setId(go.getId());

				logic.getGame().addPlayer((Player) go);
				logic.getGame().addObject(go);
				EduLog.info("Player " + ofe.getOwner() + " created");
				break;
			case YELLOWCIRCLE:
				go = new YellowCircle(logic.getGame());
				break;
			default:
				return;
			}

			for (GameEventListener gel : logic.getListenerList()) {
				gel.onObjectCreation(ofe);
			}
			// game.addObject(go);
		}

		else if (ofe.getType() == GameEventNumber.OBJECT_REMOVE) {
			int id = ofe.getId();
			ConcurrentHashMap<Integer, GameObject> gameObjects = logic
					.getGame().getObjects();

			GameObject objectToRemove = gameObjects.get(id);
			logic.getGame().removeObject(objectToRemove);

		}

	}
}
