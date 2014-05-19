package de.illonis.eduras.gameclient.gui.hud.nifty;

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
		nifty.fromXml("/res/hud/login.xml", "login", new LoginController(
				this.game));
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
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
