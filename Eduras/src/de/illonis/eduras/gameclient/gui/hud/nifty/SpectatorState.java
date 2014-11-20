package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;

/**
 * The state for ingame.
 * 
 * @author illonis
 * 
 */
public class SpectatorState extends GameState {

	public static final int SPECTATOR_STATE_ID = 8;

	SpectatorState(GameControllerBridge game) {
		super(game);
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("res/hud/spectator.xml", "spectator",
				new SpectatorController(gameBridge));
	}

	@Override
	public int getID() {
		return SPECTATOR_STATE_ID;
	}

}
