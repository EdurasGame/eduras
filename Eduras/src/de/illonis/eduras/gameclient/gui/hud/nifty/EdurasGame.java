package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;

/**
 * A test application for gui.
 * 
 * @author illonis
 * 
 */
public class EdurasGame extends NiftyStateBasedGame {

	protected EdurasGame() {
		super("title");
	}

	/**
	 * Starts the nifty test.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		EdurasGame test = new EdurasGame();
		AppGameContainer game = new AppGameContainer(test);
		game.setDisplayMode(800, 600, false);
		game.setAlwaysRender(true);
		game.start();
		game.exit();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// add game states here
		addState(new LoginState(this));
		addState(new SettingsState(this));
		
	}

}
