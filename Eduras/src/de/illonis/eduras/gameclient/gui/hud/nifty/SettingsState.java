package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;
import de.lessvoid.nifty.slick2d.NiftyGameState;

/**
 * Login game state.
 * 
 * @author illonis
 * 
 */
public class SettingsState extends NiftyBasicGameState {

	private final EdurasGame game;

	public SettingsState(EdurasGame edurasGame) {
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
		nifty.fromXml("/res/hud/settings.xml", "settings", new SettingsController(this));
	}

	public void back() {
		game.enterState(0,new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,300));

	}
}
