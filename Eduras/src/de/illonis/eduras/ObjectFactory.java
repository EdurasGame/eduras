package de.illonis.eduras;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
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
		PLAYER(1), YELLOWCIRCLE(2), NO_OBJECT(0);

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

	public void onGameEventAppeared(GameEvent event) {
		// do not handle other events in case they are received.
		if (!(event instanceof ObjectFactoryEvent))
			return;

		ObjectFactoryEvent ofe = (ObjectFactoryEvent) event;
		if (logic.getGame().getObjects().containsKey(ofe.getId()))
			return;
		GameObject go = null;
		if (ofe.getType() == GameEventNumber.OBJECT_CREATE) {
			switch (ofe.getObjectType()) {
			case PLAYER:
				System.out.println("create player: " + ofe.getOwnerId());
				go = new Player(logic.getGame(), ofe.getOwnerId());
				logic.getGame().addPlayer((Player) go);
				go.setId(ofe.getId());
				logic.getGame().addObject(go);
				break;
			case YELLOWCIRCLE:
				go = new YellowCircle(logic.getGame());
				break;
			default:
				return;
			}
			// set id if id is set in event
			if (go != null && ofe.hasId()) {
				go.setId(ofe.getId());
			}

			for (GameEventListener gel : logic.getListenerList()) {
				gel.onObjectCreation(ofe);
			}
			// game.addObject(go);
		}

		else if (ofe.getType() == GameEventNumber.OBJECT_REMOVE) {
			int id = ofe.getId();
			logic.getGame().getObjects().remove(id);
		}

	}
}
