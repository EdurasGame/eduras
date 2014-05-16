package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * First test game state.
 * 
 * @author illonis
 * 
 */
public class TestState extends NiftyOverlayBasicGameState {

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
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("/res/hud/helloworld.xml", "start");
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
		Input input = container.getInput();
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
			game.enterState(1);
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.drawString("rightclick to go to second gamestate", 150, 150);
	}
}
