package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.MessageBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class SettingsController implements ScreenController {

	private final SettingsState state;
	private Nifty nifty;

	public SettingsController(SettingsState testState) {
		this.state = testState;
	}

	/**
	 * Handles a textfield content change.<br>
	 * The annotation automatically registers the appropriate eventlistener.
	 * 
	 * @param id
	 *            the id of the textfield.
	 * @param event
	 *            the event.
	 */
	@NiftyEventSubscriber(pattern = ".*Field")
	public void onTextfieldChange(final String id,
			final TextFieldChangedEvent event) {
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;

	}

	public void exit() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		// TODO: implement
	}
	
	public void back() {
		state.back();
	}

	@Override
	public void onStartScreen() {
		// try {
		// Music openingMenuMusic = new Music("/res/music/test.ogg");
		// openingMenuMusic.loop();
		// } catch (SlickException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void onEndScreen() {
	}

}