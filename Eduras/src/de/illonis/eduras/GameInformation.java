package de.illonis.eduras;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InvalidNameException;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.items.weapons.ExampleWeapon;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;

public class GameInformation {
	private final ConcurrentHashMap<Integer, GameObject> objects;
	private final ConcurrentHashMap<Integer, Player> players;
	private final Map map;
	private EventTriggerer eventTriggerer;

	public GameInformation() {
		objects = new ConcurrentHashMap<Integer, GameObject>();
		players = new ConcurrentHashMap<Integer, Player>();
		map = new Map();

		exampleTest();
	}

	/**
	 * Just for testing purpose.
	 */
	private void exampleTest() {
		try {
			Block exampleBlock = new Block(this, map.getWidth() / 2 - 25,
					map.getHeight() / 2, 20, 20);
			addObject(exampleBlock);
			CircledBlock exampleCircledBlock = new CircledBlock(this, 20,
					map.getWidth() / 2 + 25, map.getHeight() / 2);
			addObject(exampleCircledBlock);
			ExampleWeapon exampleWeapon = new ExampleWeapon(this);
			exampleWeapon.setPosition(map.getWidth() * 0.75,
					map.getHeight() * 0.75);
			ExampleWeapon exampleWeapon2 = new ExampleWeapon(this);
			exampleWeapon2.setPosition(map.getWidth() * 0.25,
					map.getHeight() * 0.25);
			addObject(exampleWeapon);
			addObject(exampleWeapon2);
		} catch (ShapeVerticesNotApplicableException e) {
			EduLog.passException(e);
		}
	}

	/**
	 * Returns the eventtriggerer to trigger events with.
	 * 
	 * @return The eventtriggerer.
	 */
	public EventTriggerer getEventTriggerer() {
		return eventTriggerer;
	}

	/**
	 * Sets the eventtriggerer to trigger events with.
	 * 
	 * @param eventTriggerer
	 *            The eventtriggerer
	 */
	public void setEventTriggerer(EventTriggerer eventTriggerer) {
		this.eventTriggerer = eventTriggerer;
	}

	/**
	 * Returns map of current game.
	 * 
	 * @return map of current game.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Adds an object to gameobjects. Objects are put in object list assigned to
	 * their id.
	 * 
	 * @param object
	 *            new object.
	 */
	public void addObject(GameObject object) {
		objects.put(object.getId(), object);
	}

	/**
	 * Returns all game objects.
	 * 
	 * @return game object list.
	 */
	public ConcurrentHashMap<Integer, GameObject> getObjects() {
		return objects;
	}

	/**
	 * Checks if there will be a collision of the given object trying to move to
	 * the target position.
	 * 
	 * @param gameObject
	 *            The object which wants to move.
	 * @param target
	 *            The target position.
	 * @return Returns the objects position after the move. Note that the
	 *         objects new position won't be set.
	 */
	@Deprecated
	public Vector2D checkCollision(GameObject gameObject, Vector2D target) {
		ObjectShape shape = gameObject.getShape();
		Vector2D result = shape.checkCollision(this, gameObject, target);
		return result;
	}

	/**
	 * Returns gameobject with given id. If no object is found, null is
	 * returned.
	 * 
	 * @param id
	 *            id to search for.
	 * @return object with given id.
	 */
	public GameObject findObjectById(int id) {
		return objects.get(id);
	}

	/**
	 * Removes the first occurrence of the specified game object from gameobject
	 * list, if it is present. If the list does not contain the element, it is
	 * unchanged. More formally, removes the element with the lowest index
	 * <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
	 * (if such an element exists). Returns <tt>true</tt> if this list contained
	 * the specified element (or equivalently, if this list changed as a result
	 * of the call). If the game object is of type 'Player' the player is also
	 * removed from the players list.
	 * 
	 * @param go
	 *            element to be removed from this list, if present
	 * @return <tt>true</tt> if this list contained the specified element
	 */
	public boolean removeObject(GameObject go) {

		// FIXME: there is tried to remove null-objects.
		if (go == null)
			return true;
		boolean playerRemoveSuccess = true;

		if (go instanceof Player) {
			playerRemoveSuccess = players.remove(go.getOwner()) != null;
		}

		return playerRemoveSuccess && (objects.remove(go.getId()) != null);
	}

	/**
	 * Adds a player to playerlist. Players are stored assigned to their
	 * owner-id.
	 * 
	 * @param player
	 *            player to add.
	 */
	public void addPlayer(Player player) {
		players.put(player.getOwner(), player);
	}

	/**
	 * Returns a specific player identified by owner id.
	 * 
	 * @param ownerId
	 *            owner id of player.
	 * @return player object of given owner.
	 * @throws ObjectNotFoundException
	 *             Thrown if there is no object found
	 */
	public Player getPlayerByOwnerId(int ownerId)
			throws ObjectNotFoundException {
		Player result = players.get(ownerId);
		if (result == null) {
			throw new ObjectNotFoundException(ownerId);
		}
		return players.get(ownerId);
	}

	/**
	 * Returns a specific player identified by object id.
	 * 
	 * @param objectId
	 *            The id of the object which is the player.
	 * @return Returns the player object relating to the given id.
	 * @throws ObjectNotFoundException
	 *             Thrown if there could be no player found that is related to
	 *             the given id.
	 */
	public Player getPlayerByObjectId(int objectId)
			throws ObjectNotFoundException {
		Player result = null;
		for (Player singlePlayer : players.values()) {
			if (singlePlayer.getId() == objectId) {
				result = singlePlayer;
				break;
			}
		}

		if (result == null) {
			throw new ObjectNotFoundException(objectId);
		}

		return result;

	}

	/**
	 * This method serializes all available current information about the game
	 * into events and returns them as a list.
	 */
	public ArrayList<GameEvent> getAllInfosAsEvent() {

		ArrayList<GameEvent> infos = new ArrayList<GameEvent>();

		for (GameObject object : objects.values()) {
			ObjectFactoryEvent objectEvent = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_CREATE, object.getType());
			objectEvent.setOwner(object.getOwner());
			objectEvent.setId(object.getId());
			infos.add(objectEvent);

			// send position immediately
			MovementEvent me = new MovementEvent(GameEventNumber.SET_POS,
					object.getId());
			me.setNewXPos(object.getXPosition());
			me.setNewYPos(object.getYPosition());
			infos.add(me);

			// send visible / collidable status
			SetBooleanGameObjectAttributeEvent visEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_VISIBLE, object.getId(),
					object.isVisible());
			SetBooleanGameObjectAttributeEvent colEvent = new SetBooleanGameObjectAttributeEvent(
					GameEventNumber.SET_COLLIDABLE, object.getId(),
					object.isCollidable());
			infos.add(colEvent);
			infos.add(visEvent);
		}
		for (Player p : players.values()) {
			try {
				infos.add(new ClientRenameEvent(p.getOwner(), p.getName()));
			} catch (InvalidNameException e) {
				EduLog.passException(e);
				continue;
			}
		}

		return infos;
	}

}
