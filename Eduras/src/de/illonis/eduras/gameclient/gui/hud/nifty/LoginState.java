package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.util.concurrent.Callable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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

	public LoginState(GameControllerBridge game) {
		this.game = game;
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game) {
		initNifty(container, game);

		try {
			SoundMachine.init();
		} catch (SlickException e1) {
		}

	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		controller = new LoginController(
				this.game);
		nifty.fromXml("/res/hud/login.xml", "login", controller);
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
		controller.update();
		// Input input = container.getInput();
		// if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
		// game.enterState(1);
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.setColor(Color.white);
		g.drawString("Welcome to Eduras!", 150, 150);
	}
	
}
