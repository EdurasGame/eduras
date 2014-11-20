package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.lessvoid.nifty.screen.Screen;

/**
 * The controller for ingame state is empty as we have our own game logic.
 * 
 * @author illonis
 * 
 */
public class SpectatorController extends EdurasScreenController {

	SpectatorController(GameControllerBridge game) {
		super(game);
	}

	@Override
	protected void initScreen(Screen screen) {
	}

}
