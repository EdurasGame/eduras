package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * Login game state.
 * 
 * @author illonis
 * 
 */
public class SettingsState extends NiftyOverlayBasicGameState {

	private final GameControllerBridge game;
	private SettingsController controller;

	SettingsState(GameControllerBridge edurasGame) {
		this.game = edurasGame;
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void enterState(GameContainer container, StateBasedGame stateGame) {
		controller.enterState();
	}

	@Override
	public void leaveState(GameContainer container, StateBasedGame stateGame) {
		controller.leaveState();
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame stateGame) {
		controller = new SettingsController(this.game);
		nifty.fromXml("res/hud/settings.xml", "settings", controller);
	}

	@Override
	public void keyPressed(int key, char c) {
		controller.keyPressed(key);
	}

	@Override
	protected void initGameAndGUI(GameContainer container,
			StateBasedGame stateGame) {
		initNifty(container, stateGame);
	}

	@Override
	protected void renderGame(GameContainer container,
			StateBasedGame stateGame, Graphics g) {
	}

	@Override
	protected void updateGame(GameContainer container,
			StateBasedGame stateGame, int delta) {
	}

}
