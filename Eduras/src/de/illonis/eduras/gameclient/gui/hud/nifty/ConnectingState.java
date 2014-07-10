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
public class ConnectingState extends NiftyOverlayBasicGameState {

	private final GameControllerBridge gameBridge;
	private ConnectingController controller;

	ConnectingState(GameControllerBridge game) {
		this.gameBridge = game;
	}

	@Override
	public int getID() {
		return 5;
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		controller = new ConnectingController(this.gameBridge);
		nifty.fromXml("/res/hud/connecting.xml", "connecting", controller);
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
		controller.update();
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) {
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game) {
		controller.setConnect(true);
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game) {
		initNifty(container, game);
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) {
	}
}
