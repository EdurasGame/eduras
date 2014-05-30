package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.json.JSONObject;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
import de.illonis.eduras.utils.WebFetcher;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class LoginController extends EdurasScreenController {

	private Callable<LoginResult> loginCallable;
	private Future<LoginResult> loginFuture = null;
	private boolean login = false;
	private final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(
			2);

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
		nameField.setFocus();
	}

	public void login() {
		setControlsEnabled(false);
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		String username = nameField.getRealText();
		String password = passwordField.getRealText();
		if (username.length() < 3) {
			nameNote.setText("Username too short.");
			setControlsEnabled(true);
			nameField.setFocus();
			return;
		}
		if (password.length() < 5) {
			nameNote.setText("Password too short.");
			setControlsEnabled(true);
			passwordField.setFocus();					
			return;
		}
		
		loginCallable = new LoginTask(username, password);
		login = true;
	}

	private void setControlsEnabled(boolean enabled) {
		nameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		loginButton.setEnabled(enabled);
	}

	public void update() {
		if (login) {
			if (loginFuture == null) {
				// if we have not started loading yet, submit the Callable to
				// the executor
				loginFuture = exec.submit(loginCallable);
			}
			// check if the execution on the other thread is done
			if (loginFuture.isDone()) {
				// these calls have to be done on the update loop thread,
				// especially attaching the terrain to the rootNode
				// after it is attached, it's managed by the update loop thread
				// and may not be modified from any other thread anymore!

				login = false;
				LoginResult result;
				try {
					result = loginFuture.get();
					if (result.success) {
						game.setUsername(nameField.getRealText());
						game.enterState(2);
					} else {
						nameNote.setText("Login failed.");
						setControlsEnabled(true);
						passwordField.setText("");
						passwordField.setFocus();					
						// login failed
					}
				} catch (InterruptedException | ExecutionException e) {
					nameNote.setText(e.getMessage());
				}
				loginFuture = null;
			}
		}
	}

	private static class LoginResult {
		boolean success;
		String message;
	}

	static class LoginTask implements Callable<LoginResult> {

		private final static URL AUTH_URL = createAuthURL("http://illonis.de/eduras/auth/");

		private static URL createAuthURL(final String name) {
			try {
				return new URL(name);
			} catch (final MalformedURLException ex) {
				throw new Error(ex);
			}
		}

		private final static String USERNAME_FIELD = "user";
		private final static String PASSWORD_FIELD = "password";
		private final static String CHANNEL_FIELD = "rel";
		private final static String CHANNEL_VALUE = "beta";

		private final String username;
		private final String password;

		public LoginTask(String username, String password) {
			this.username = username;
			this.password = password;
		}

		@Override
		public LoginResult call() throws Exception {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put(USERNAME_FIELD, username);
			data.put(PASSWORD_FIELD, password);
			data.put(CHANNEL_FIELD, CHANNEL_VALUE);
			String result = WebFetcher.post(AUTH_URL, data);
			JSONObject obj = new JSONObject(result);
			LoginResult resultObject = new LoginResult();
			resultObject.success = obj.getBoolean("success");
			return resultObject;
		}

	}

}