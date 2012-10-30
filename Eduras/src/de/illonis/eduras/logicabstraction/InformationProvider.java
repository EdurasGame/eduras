/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import de.illonis.eduras.Game;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.InfoInterface;

/**
 * This class provides a connection between GUI and logic. GUI developers can
 * use the methods provided here to gain information about the current state of
 * the game to use these in your GUI.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InformationProvider implements InfoInterface {

	private final GameLogicInterface logic;
	
	/**
	 * Creates a new InformationProvider that gains information with the given logic.
	 * @param logic The logic to gain information from.
	 */
	InformationProvider(GameLogicInterface logic){
		this.logic = logic;
	}
	
	/* (non-Javadoc)
	 * @see de.illonis.eduras.interfaces.InfoInterface#getMapBounds()
	 */
	@Override
	public int[] getMapBounds() {
		// TODO IMPLEMENT!
		return null;
	}

	/**
	 * @return
	 */
	public Game getGame() {
		return logic.getGame();
	}
	
	/**
	 * Adds an eventlistener.
	 * @param listener The listener.
	 */
	public void addEventListener(GameEventListener listener) {
		logic.addGameEventListener(listener);
	}

}
