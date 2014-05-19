package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class LoginController extends EdurasScreenController {

	private Button loginButton;
	private TextField nameField;
	private TextField passwordField;
	private Label nameNote;
	private Label passwordNote;
	Element popupElement;

	public LoginController(GameControllerBridge game) {
		super(game);
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

	protected void initScreen(Screen screen) {
		passwordField = screen.findNiftyControl("passwordField",
				TextField.class);
		passwordField.setFormat(new FormatPassword());
		popupElement = nifty.createPopup("niftyPopupMenu");

		nameField = screen.findNiftyControl("userNameField", TextField.class);
		nameNote = screen.findNiftyControl("userNameNote", Label.class);
		passwordNote = screen.findNiftyControl("passwordNote", Label.class);
		loginButton = screen.findNiftyControl("loginButton", Button.class);
	}

	public void login() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		setControlsEnabled(false);
		game.enterState(2);
	}

	private void setControlsEnabled(boolean enabled) {
		nameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		loginButton.setEnabled(enabled);
	}

}