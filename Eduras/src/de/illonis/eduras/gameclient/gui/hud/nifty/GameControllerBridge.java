package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

/**
 * Provides some access to the game itself from controllers.
 * 
 * @author illonis
 * 
 */
public interface GameControllerBridge {

	void exit();

	void enterState(int state);
	
	void enterState(int id, Transition leave, Transition enter);
	
	void changeResolution(int width, int height) throws SlickException;

}
