package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;

/**
 * Login game state.
 * 
 * @author illonis
 * 
 */
public class SettingsState extends NiftyBasicGameState {

	private final GameControllerBridge game;
	private SettingsController controller;

	SettingsState(GameControllerBridge edurasGame) {
		super("settings");
		this.game = edurasGame;
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void enterState(GameContainer container, StateBasedGame stateGame) {
	}

	@Override
	public void leaveState(GameContainer container, StateBasedGame stateGame) {
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame stateGame) {
		controller = new SettingsController(this.game);
		nifty.fromXml("/res/hud/settings.xml", "settings", controller);
	}

	@Override
	public void keyPressed(int key, char c) {
		controller.keyPressed(key);
	}

}
