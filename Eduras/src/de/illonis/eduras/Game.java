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

}
