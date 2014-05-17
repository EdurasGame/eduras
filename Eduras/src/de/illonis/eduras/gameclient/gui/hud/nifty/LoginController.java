package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.MessageBox;
import de.lessvoid.nifty.controls.MessageBox.MessageType;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.messagebox.builder.MessageBoxBuilder;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LoginController implements ScreenController {

	private final LoginState state;
	private Nifty nifty;

	private Button loginButton;
	private Button settingsButton;
	private TextField nameField;
	private TextField passwordField;
	private Label nameNote;
	private Label passwordNote;
	Element popupElement;
	
	public LoginController(LoginState testState) {
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
		passwordField = screen.findNiftyControl("passwordField",
				TextField.class);
		passwordField.setFormat(new FormatPassword());
		popupElement = nifty.createPopup("niftyPopupMenu");

		nameField = screen.findNiftyControl("userNameField", TextField.class);
		nameNote = screen.findNiftyControl("userNameNote", Label.class);
		passwordNote = screen.findNiftyControl("passwordNote", Label.class);
		loginButton = screen.findNiftyControl("loginButton", Button.class);
		settingsButton = screen
				.findNiftyControl("settingsButton", Button.class);

		

	}

	public void login() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		setControlsEnabled(false);
	}

	private void setControlsEnabled(boolean enabled) {
		nameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		loginButton.setEnabled(enabled);
		settingsButton.setEnabled(enabled);
	}

	public void showSettings() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		state.showSettings();
	}

	public void exit() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		// TODO: implement
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
	
	public void ok() {
		System.out.println("ok there");
	}
	
	class ExitConfirmDialog extends MessageBox {
		public ExitConfirmDialog(Nifty nifty, MessageType type,
				String title, String[] buttons, String image) {
			super(nifty, type, title, buttons, image);
		
		}

		
		public void ok() {
			System.out.println("ok here");
		}
		@Override
		public boolean inputEvent(NiftyInputEvent inputEvent) {
			System.out.println(inputEvent.getClass());
			return super.inputEvent(inputEvent);
		}
	}
}