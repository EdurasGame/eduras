package de.illonis.eduras;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InvalidNameException;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;

public class GameInformation {
	private final HashMap<Integer, GameObject> objects;
	private final HashMap<Integer, Player> players;
	private final Map map;

	public GameInformation() {
		objects = new HashMap<Integer, GameObject>();
		players = new HashMap<Integer, Player>();
		map = new Map();
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
	public HashMap<Integer, GameObject> getObjects() {
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
	 * of the call).
	 * 
	 * @param go
	 *            element to be removed from this list, if present
	 * @return <tt>true</tt> if this list contained the specified element
	 */
	public boolean removeObject(GameObject go) {
		return (objects.remove(go.getId()) != null);
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
	 */
	public Player getPlayerByOwnerId(int ownerId) {
		return players.get(ownerId);
	}

	/**
	 * This method serializes all available current information about the game
	 * into events and returns them as a list.
	 */
	public ArrayList<GameEvent> getAllInfosAsEvent() {

		ArrayList<GameEvent> infos = new ArrayList<GameEvent>();

		for (GameObject object : objects.values()) {
			ObjectFactoryEvent objectEvent = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_CREATE, ObjectType.PLAYER);
			objectEvent.setOwner(object.getOwner());
			objectEvent.setId(object.getId());
			infos.add(objectEvent);

			// send position immediately
			MovementEvent me = new MovementEvent(GameEventNumber.SET_POS,
					object.getId());
			me.setNewXPos(object.getXPosition());
			me.setNewYPos(object.getYPosition());
			infos.add(me);
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
