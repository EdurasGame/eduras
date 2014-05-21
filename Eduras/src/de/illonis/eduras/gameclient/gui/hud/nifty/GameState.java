package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * The state for ingame.
 * 
 * @author illonis
 * 
 */
public class GameState extends NiftyOverlayBasicGameState {
	
	private final GameControllerBridge gameBridge;
	
	public GameState(GameControllerBridge game) {
		this.gameBridge = game;
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game) {
		initNifty(container, game);
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) {
		// TODO: implement
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
		// TODO: implement
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("/res/hud/game.xml", "game", new GameController(gameBridge));
	}

	@Override
	public int getID() {
		return 4;
	}

}
