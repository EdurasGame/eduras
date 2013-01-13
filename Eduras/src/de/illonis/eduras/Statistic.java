package de.illonis.eduras;

import java.util.HashMap;

public class Statistic {

	private HashMap<Player, Integer> killsOfPlayer;

	public Statistic() {
		killsOfPlayer = new HashMap<Player, Integer>();
	}

	public int getKillsOfPlayer(Player player) {
		return killsOfPlayer.get(player);
	}

}
