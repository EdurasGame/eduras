package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * Controller for login screen.
 * 
 * @author illonis
 * 
 */
public class LoginController extends EdurasScreenController {

	private Callable<LoginResult> loginCallable;
	private Future<LoginResult> loginFuture = null;
	private boolean login = false;
	private final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(
			2);

	private Button loginButton;
	private TextField nameField;
	// private TextField passwordField;
	private Label nameNote;
	Element popupElement;

	private final String presetAccountName;
	private final String presetAccountPassword;

	LoginController(GameControllerBridge game) {
		super(game);

		presetAccountName = "";
		presetAccountPassword = "";
	}

	LoginController(GameControllerBridge game, String betaAccountName,
			String betaAccountPassword) {
		super(game);

		presetAccountName = betaAccountName;
		presetAccountPassword = betaAccountPassword;
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
	protected void initScreen(Screen screen) {
		/*
		 * passwordField = screen.findNiftyControl("passwordField",
		 * TextField.class); passwordField.setFormat(new FormatPassword());
		 * passwordField.setText(presetAccountPassword);
		 */
		popupElement = nifty.createPopup("niftyPopupMenu");

		nameField = screen.findNiftyControl("userNameField", TextField.class);
		nameField.setText(presetAccountName);
		nameNote = screen.findNiftyControl("userNameNote", Label.class);
		loginButton = screen.findNiftyControl("loginButton", Button.class);
		nameField.setFocus();

		if (!presetAccountName.isEmpty() && !presetAccountPassword.isEmpty()) {
			login();
		}
	}

	/**
	 * Performs login operation asynchronously.
	 */
	public void login() {
		setControlsEnabled(false);
		SoundMachine.play(SoundType.CLICK, 2f);
		String username = nameField.getRealText();
		// String password = passwordField.getRealText();
		if (username.length() < 3) {
			nameNote.setText("Username too short.");
			setControlsEnabled(true);
			nameField.setFocus();
			return;
		}
		/*
		 * if (password.length() < 5) { nameNote.setText("Password too short.");
		 * setControlsEnabled(true); passwordField.setFocus(); return; }
		 */
		nameNote.setText("Connecting...");
		loginCallable = new LoginTask(username, "");
		login = true;
	}

	private void setControlsEnabled(boolean enabled) {
		nameField.setEnabled(enabled);
		// passwordField.setEnabled(enabled);
		loginButton.setEnabled(enabled);
	}

	/**
	 * Handles login result
	 */
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
						game.enterState(ServerListState.SERVER_LIST_STATE_ID);
					} else {
						nameNote.setText("Login failed.");
						// passwordField.setText("");
						// passwordField.setFocus();
						// login failed
					}
				} catch (InterruptedException | ExecutionException e) {
					nameNote.setText(e.getMessage());
				}
				setControlsEnabled(true);
				loginFuture = null;
			}
		}
	}

	private static class LoginResult {
		boolean success;
	}

	static class LoginTask implements Callable<LoginResult> {

		private final static URL AUTH_URL = createAuthURL(
				"http://illonis.de/eduras/beta/auth");

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
			LoginResult resultObject = new LoginResult();
			resultObject.success = true;
			return resultObject;
			/*
			 * try { String result = WebFetcher.post(AUTH_URL, data); JSONObject
			 * obj = new JSONObject(result);
			 * 
			 * resultObject.success = obj.getBoolean("success"); } catch
			 * (IOException e) { throw new ExecutionException(
			 * "Login server not available.", e); } return resultObject;
			 */

		}
	}
}
