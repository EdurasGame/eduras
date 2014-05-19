package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;

/**
 * Login game state.
 * 
 * @author illonis
 * 
 */
public class SettingsState extends NiftyBasicGameState {

	private final GameControllerBridge game;

	public SettingsState(GameControllerBridge edurasGame) {
		super("settings");
		this.game = edurasGame;
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void enterState(GameContainer container, StateBasedGame game) {
	}

	@Override
	public void leaveState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("/res/hud/settings.xml", "settings",
				new SettingsController(this.game));
	}

}
