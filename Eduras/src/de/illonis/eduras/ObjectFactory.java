package de.illonis.eduras;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.DataMissingException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * ObjectFactory is in charge of handling Objectfactory events and creating and
 * removing of objects.
 * 
 * @author illonis
 * 
 */
public class ObjectFactory {
	private final static Logger L = EduLog.getLoggerFor(ObjectFactory.class
			.getName());

	private final GameLogicInterface logic;
	private final TimingSource timingSource;

	/**
	 * Collection of object types that can be created by {@link ObjectFactory}.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum ObjectType {
		PLAYER(1), YELLOWCIRCLE(2), SIMPLEMISSILE(3), ITEM_WEAPON_SIMPLE(4),
		NO_OBJECT(0), BIGBLOCK(5), SMALLCIRCLEDBLOCK(6), SNIPERMISSILE(7),
		ITEM_WEAPON_SNIPER(8), BUILDING(9), BIGGERBLOCK(10),
		ITEM_WEAPON_SPLASH(11), MISSILE_SPLASH(12), MISSILE_SPLASHED(13),
		DYNAMIC_POLYGON_BLOCK(14), ITEM_WEAPON_SWORD(15), SWORDMISSILE(16),
		BIRD(17), ROCKETLAUNCHER(18), ROCKET_MISSILE(19), MINELAUNCHER(20),
		MINE_MISSILE(21), ASSAULTRIFLE(22), ASSAULT_MISSILE(23), MAPBOUNDS(24),
		TRIGGER_AREA(25), NEUTRAL_BASE(26), OBSERVER(30), SPELL_SCOUT(31),
		HEALING_POTION(32), SPEED_POWERUP(33), INVISIBILITY_POWERUP(34),
		PORTAL(35);

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

		public boolean isItem() {
			switch (this) {
			case ITEM_WEAPON_SIMPLE:
			case ITEM_WEAPON_SNIPER:
			case ITEM_WEAPON_SPLASH:
			case ITEM_WEAPON_SWORD:
			case ROCKETLAUNCHER:
			case MINELAUNCHER:
			case ASSAULTRIFLE:
				return true;
			default:
				return false;
			}
		}

		public boolean isUnit() {
			switch (this) {
			case SPELL_SCOUT:
			case OBSERVER:
				return true;
			default:
				return false;
			}
		}

		public int getCosts() {
			switch (this) {
			case OBSERVER:
				return S.Server.unit_observer_costs;
			case ITEM_WEAPON_SIMPLE:
				return S.Server.go_simpleweapon_costs;
			case ITEM_WEAPON_SNIPER:
				return S.Server.go_sniperweapon_costs;
			case ITEM_WEAPON_SPLASH:
				return S.Server.go_splashweapon_costs;
			case ITEM_WEAPON_SWORD:
				return S.Server.go_swordweapon_costs;
			case ROCKETLAUNCHER:
				return S.Server.go_rocketlauncher_costs;
			case MINELAUNCHER:
				return S.Server.go_mineweapon_costs;
			case ASSAULTRIFLE:
				return S.Server.go_assaultrifle_costs;
			default:
				return 0;

			}
		}

		public static LinkedList<ObjectType> getItemTypes() {
			LinkedList<ObjectType> itemTypes = new LinkedList<ObjectType>();
			for (ObjectType someObjectType : ObjectType.values()) {
				if (someObjectType.isItem()) {
					itemTypes.add(someObjectType);
				}
			}

			return itemTypes;
		}
	}

	/**
	 * Creates a new objectfactory for given game.
	 * 
	 * @param logic
	 *            game object factory is assigned to.
	 * @param source
	 *            a timing source that will be attached to created objects.
	 */
	public ObjectFactory(GameLogicInterface logic, TimingSource source) {
		this.logic = logic;
		this.timingSource = source;
	}

	/**
	 * Handles an object attribute changed event.
	 * 
	 * @param event
	 *            {@link SetGameObjectAttributeEvent} that occured.
	 */
	public void onObjectAttributeChanged(SetGameObjectAttributeEvent<?> event) {
		logic.getGame().getEventTriggerer().notifyGameObjectStateChanged(event);
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
				L.log(Level.SEVERE, "Event has no id",
						new DataMissingException("No id assigned!"));
				return;
			}

			switch (event.getObjectType()) {
			case PLAYER:
				Player playerForMainFigure;
				try {
					playerForMainFigure = logic.getGame().getPlayerByOwnerId(
							owner);
				} catch (ObjectNotFoundException e1) {
					L.log(Level.SEVERE,
							"There is no player for the given owner!", e1);
					return;
				}

				go = new PlayerMainFigure(logic.getGame(), timingSource,
						event.getOwner(), id, playerForMainFigure);
				playerForMainFigure.setPlayerMainFigure((PlayerMainFigure) go);

				L.info("Player " + event.getOwner() + " created");
				// testSpawnBird();
				break;
			default:
				try {
					go = ObjectCreator.createObject(event.getObjectType(),
							logic.getGame(), timingSource, id);
				} catch (FactoryException | ShapeVerticesNotApplicableException e) {
					L.log(Level.SEVERE, "Could not create object", e);
				}
				if (owner != -1)
					go.setOwner(owner);
				break;
			}
			if (go != null) {
				go.setId(id);
				go.setOwner(owner);
				logic.getGame().addObject(go);

				logic.getGame().getEventTriggerer().notifyObjectCreated(event);
				logic.getListener().onObjectCreation(event);
			}

		}

		else if (event.getType() == GameEventNumber.OBJECT_REMOVE) {
			int id = event.getId();

			GameObject objectToRemove = logic.getGame().getObjects().get(id);
			if (objectToRemove == null)
				return;

			// rocket splash animation
			// if (objectToRemove.getType() == ObjectType.ROCKET_MISSILE
			// || objectToRemove.getType() == ObjectType.MINE_MISSILE) {
			// // meeh, this violates the rule to strictly seperate GUI from
			// // logic. We should call a handler here.
			// AnimationFactory.runAt(AnimationNumber.ROCKET_SPLASH,
			// objectToRemove.getPositionVector(),
			// S.Server.go_rocketmissile_damageradius);
			// }
			logic.getListener().onObjectRemove(event);
			logic.getGame().removeObject(objectToRemove);
		}
	}

	// a very hacky test method to spawn a bird when a player joins.
	private void testSpawnBird() {
		ObjectFactoryEvent birdEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, ObjectType.BIRD, -1);
		birdEvent.setId(999);

		int w = logic.getGame().getMap().getWidth() / 2;
		int h = logic.getGame().getMap().getHeight() / 2;
		onObjectFactoryEventAppeared(birdEvent);

		GameObject o;
		try {
			o = logic.getGame().findObjectById(999);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find bird!", e);
			return;
		}
		o.setPosition(w, h);
		LinkedList<Integer> units = new LinkedList<Integer>();
		units.add(999);
		SendUnitsEvent sendEvent = new SendUnitsEvent(-1, new Vector2df(),
				units);
		logic.onGameEventAppeared(sendEvent);
	}
}
