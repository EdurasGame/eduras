package de.illonis.eduras;

import java.util.ArrayList;

public class Game {
	private GameObject player1;
	private ArrayList<GameObject> objects;

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

}
