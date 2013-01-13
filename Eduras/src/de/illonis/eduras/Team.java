package de.illonis.eduras;

import java.util.LinkedList;

public class Team {

	private LinkedList<Player> players;

	public Team(LinkedList<Player> players) {
		this.players = players;
	}

	public void addPlayer(Player newPlayer) {
		players.add(newPlayer);
	}

	public void removePlayer(Player playerToRemove) {
		players.remove(playerToRemove);
	}

}
