package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * Login game state.
 * 
 * @author illonis
 * 
 */
public class LoginState extends NiftyOverlayBasicGameState {

	private final GameControllerBridge game;
	private LoginController controller;

	LoginState(GameControllerBridge game) {
		this.game = game;
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame stateGame) {
	}

	@Override
	protected void initGameAndGUI(GameContainer container,
			StateBasedGame stateGame) {
		initNifty(container, stateGame);
		try {
			SoundMachine.init();
		} catch (SlickException e1) {
		}
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame stateGame) {
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			controller.login();
			return;
		}
		super.keyReleased(key, c);
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame stateGame) {
		controller = new LoginController(game);
		nifty.fromXml("/res/hud/login.xml", "login", controller);
	}

	@Override
	protected void updateGame(GameContainer container,
			StateBasedGame stateGame, int delta) {
		controller.update();
	}

	@Override
	protected void renderGame(GameContainer container,
			StateBasedGame stateGame, Graphics g) {
		g.setColor(Color.white);
		g.drawString("Welcome to Eduras!", 150, 150);
	}

}
