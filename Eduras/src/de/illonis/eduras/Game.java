package de.illonis.eduras;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Game {
	private GameObject player1;
	private final ArrayList<GameObject> objects;

	public Game() {
		objects = new ArrayList<GameObject>();
	}

	public ArrayList<GameObject> getObjects() {
		return objects;
	}

	public GameObject getPlayer1() {
		return player1;
	}

	public void setPlayer1(GameObject player1) {
		this.player1 = player1;
	}

	public Point2D.Double checkCollision(GameObject gameObject,
			Point2D.Double target) {
		// TODO: Implement!
		return target;
	}

}
