package de.illonis.eduras;

import java.util.ArrayList;

import de.illonis.eduras.math.Vector2D;

public class Game {
	private Player player1;
	private final ObjectFactory factory;
	private final ArrayList<GameObject> objects;

	public ObjectFactory getFactory() {
		return factory;
	}

	public Game() {
		factory = new ObjectFactory(this);
		objects = new ArrayList<GameObject>();
	}

	public void addObject(GameObject object) {
		objects.add(object);
	}

	public ArrayList<GameObject> getObjects() {
		return objects;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * Checks if there will be a collision of the given object trying to move to the target position.
	 * @param gameObject The object which wants to move.
	 * @param target The target position.
	 * @return Returns the objects position after the move. Note that the objects new position won't be set.
	 */
	public Vector2D checkCollision(GameObject gameObject,
			Vector2D target) {
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
		for (GameObject o : objects)
			if (o.getId() == id)
				return o;
		return null;
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
		return objects.remove(go);
	}

}
