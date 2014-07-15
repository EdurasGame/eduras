package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * An abstract screen controller that covers some basic features. All
 * {@link ScreenController} for Eduras should extend from this.
 * 
 * @author illonis
 * 
 */
public abstract class EdurasScreenController implements ScreenController {

	protected final GameControllerBridge game;
	protected Nifty nifty;

	/**
	 * @param game
	 *            the bridge to the game.
	 */
	public EdurasScreenController(GameControllerBridge game) {
		this.game = game;
	}

	@Override
	public final void bind(Nifty niftyObject, Screen screen) {
		this.nifty = niftyObject;
		initScreen(screen);
	}

	/**
	 * Setup your fields here. This is called immediately after the
	 * {@link #bind(Nifty, Screen)} method.<br>
	 * Use the {@link #nifty} field to refer to nifty.
	 * 
	 * @param screen
	 *            the screen to bind to.
	 */
	protected abstract void initScreen(Screen screen);

	@Override
	public void onStartScreen() {
	}

	@Override
	public void onEndScreen() {
	}

	/**
	 * Quits the game.
	 */
	public void exit() {
		SoundMachine.play(SoundType.CLICK, 2f);
		game.exit();
	}

}
