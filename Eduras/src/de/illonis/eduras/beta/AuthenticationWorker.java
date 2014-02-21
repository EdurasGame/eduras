package de.illonis.eduras.beta;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.SwingWorker;

import org.json.JSONObject;

import de.illonis.eduras.utils.WebFetcher;

/**
 * Authenticates against server in background.
 * 
 * @author illonis
 * 
 */
public class AuthenticationWorker extends SwingWorker<Boolean, Void> {

	private final static URL AUTH_URL = createAuthURL("http://illonis.dyndns.org/eduras/auth/");

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
	private final static String CHANNEL_VALUE = "be1ta";

	private final String username;
	private final String password;

	AuthenticationWorker(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(USERNAME_FIELD, username);
		data.put(PASSWORD_FIELD, password);
		data.put(CHANNEL_FIELD, CHANNEL_VALUE);
		String result = WebFetcher.post(AUTH_URL, data);
		JSONObject obj = new JSONObject(result);
		return obj.getBoolean("success");
	}
}