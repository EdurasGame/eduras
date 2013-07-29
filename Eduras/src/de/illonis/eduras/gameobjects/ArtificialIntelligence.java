package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;

public abstract class ArtificialIntelligence implements Runnable {

	private GameInformation gameInfo;
	private ActiveGameObject activeGameObject;

	public ArtificialIntelligence(GameInformation gameInfo, ActiveGameObject o) {
		this.gameInfo = gameInfo;
		this.activeGameObject = o;
	}

	public GameInformation getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
	}

	public ActiveGameObject getActiveGameObject() {
		return activeGameObject;
	}

	public void setActiveGameObject(ActiveGameObject activeGameObject) {
		this.activeGameObject = activeGameObject;
	}

}
