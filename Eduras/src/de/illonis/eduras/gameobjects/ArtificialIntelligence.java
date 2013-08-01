package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;

/**
 * An Artificial Intelligence is a thread that computes the behavior of an
 * object.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ArtificialIntelligence implements Runnable {

	private GameInformation gameInfo;
	private ActiveGameObject activeGameObject;

	/**
	 * Create an ArtificialIntelligence that belongs to the given gameobject and
	 * computes action considering the game info.
	 * 
	 * @param gameInfo
	 * @param o
	 */
	public ArtificialIntelligence(GameInformation gameInfo, ActiveGameObject o) {
		this.gameInfo = gameInfo;
		this.activeGameObject = o;
	}

	/**
	 * Returns the game information.
	 * 
	 * @return Teh gameInfoz.
	 */
	public GameInformation getGameInfo() {
		return gameInfo;
	}

	/**
	 * Sets the game information
	 * 
	 * @param gameInfo
	 */
	public void setGameInfo(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
	}

	/**
	 * Returns the game object this AI belongs to.
	 * 
	 * @return The object
	 */
	public ActiveGameObject getActiveGameObject() {
		return activeGameObject;
	}

	/**
	 * Sets the game object this AI belongs to.
	 * 
	 * @param activeGameObject
	 */
	public void setActiveGameObject(ActiveGameObject activeGameObject) {
		this.activeGameObject = activeGameObject;
	}

}
