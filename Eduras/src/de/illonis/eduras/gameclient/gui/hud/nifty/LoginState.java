package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.awt.SplashScreen;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * Login game state.
 * 
 * @author illonis
 * 
 */
public class LoginState extends NiftyOverlayBasicGameState {

	public static final int LOGIN_STATE_ID = 0;
	private final GameControllerBridge game;
	private LoginController controller;
	private final String betaAccountName;
	private final String betaAccountPassword;

	LoginState(GameControllerBridge game) {
		this.game = game;

		betaAccountName = "";
		betaAccountPassword = "";
	}

	LoginState(GameControllerBridge game, String betaAccountName,
			String betaAccountPassword) {
		this.game = game;

		this.betaAccountName = betaAccountName;
		this.betaAccountPassword = betaAccountPassword;
	}

	@Override
	public int getID() {
		return LOGIN_STATE_ID;
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame stateGame) {
		SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash != null) {
			splash.close();
		}
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
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame stateGame) {
		controller = new LoginController(game, betaAccountName,
				betaAccountPassword);
		nifty.fromXml("res/hud/login.xml", "login", controller);
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
