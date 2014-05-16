package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * The second gamestate. This state has no controller assigned as no event
 * interaction is required.
 * 
 * @author illonis
 * 
 */
public class TestState2 extends NiftyOverlayBasicGameState {

	@Override
	public int getID() {
		return 1;
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game) {
		initNifty(container, game);
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("/res/hud/secondworld.xml", "stop");
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) {

	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
		Input input = container.getInput();
		if (input.isMouseButtonDown(Input.MOUSE_MIDDLE_BUTTON))
			game.enterState(0);
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game) {
		System.out.println("entered state 2");
	}
}
