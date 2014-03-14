package de.illonis.eduras.beta;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Provides beta authentication.
 * 
 * @author illonis
 * 
 */
public class BetaAuthenticator {

	private final AuthenticationForm frame;
	private AuthenticationWorker worker;
	private boolean result;
	private int attemptsLeft;

	/**
	 * Creates a new authenticator.
	 */
	public BetaAuthenticator() {
		frame = new AuthenticationForm(this);
		result = false;
		attemptsLeft = 0;
	}

	/**
	 * Authenticates the user by prompting an authentication dialog. This method
	 * blocks until user was authenticated successfully.
	 * 
	 * @param numAttempts
	 *            maximum number of authentication attempts until false is
	 *            returned. 0 for no retries.
	 * 
	 * @return true if user is authenticated successfully, false otherwise.
	 */
	public boolean authenticate(int numAttempts) {
		return authenticate(numAttempts, "", "");
	}

	/**
	 * Authenticates the user with given credentials. If credentials are
	 * invalid, the user is prompted to enter new one. This method blocks until
	 * user was authenticated successfully.
	 * 
	 * @param numAttempts
	 *            maximum number of authentication attempts until false is
	 *            returned. 0 for no retries.
	 * @param username
	 *            the initial username.
	 * @param password
	 *            the initial password.
	 * 
	 * @return true if user is authenticated successfully, false otherwise.
	 */
	public boolean authenticate(int numAttempts, String username,
			String password) {
		if (numAttempts <= 0)
			numAttempts = 1;
		attemptsLeft = numAttempts;
		if (!username.isEmpty() && !password.isEmpty()) {
			if (frame.prefill(username, password))
				authenticate(username, password.toCharArray());
		}
		frame.show();
		return result;
	}

	class AuthListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {

			if ("state".equals(event.getPropertyName())
					&& SwingWorker.StateValue.DONE == event.getNewValue()) {
				try {
					result = worker.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					result = false;
				}
				onResult(result);
			}
		}
	}

	private void onResult(boolean success) {
		attemptsLeft--;
		if (success)
			frame.hide();
		else if (attemptsLeft <= 0) {
			JOptionPane
					.showMessageDialog(null, "Maximum retries reached. Bye.");
			frame.hide();
		} else
			frame.onLoginFailed();
	}

	void authenticate(String username, char[] password) {
		frame.onLoginStart();
		worker = new AuthenticationWorker(username, new String(password));
		worker.addPropertyChangeListener(new AuthListener());
		worker.execute();
	}
}
