package de.illonis.eduras;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Game {
	private Player player1;
	private ObjectFactory factory;
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

	public Point2D.Double checkCollision(GameObject gameObject,
			Point2D.Double target) {
		// TODO: Implement!
		return target;
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
