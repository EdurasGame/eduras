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
public class NiftyTest extends NiftyStateBasedGame {

	protected NiftyTest() {
		super("title");
	}

	/**
	 * Starts the nifty test.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// just as usual as NiftyStateBasedGame extends slicks BasicGame
		NiftyTest test = new NiftyTest();
		AppGameContainer game = new AppGameContainer(test);
		game.setDisplayMode(800, 600, false);
		game.setAlwaysRender(true);
		game.start();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// add game states here
		addState(new TestState());
		addState(new TestState2());
	}

}
