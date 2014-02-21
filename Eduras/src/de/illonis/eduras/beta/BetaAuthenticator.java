package de.illonis.eduras.beta;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

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
	private int retriesLeft;

	/**
	 * Creates a new authenticator.
	 */
	public BetaAuthenticator() {
		frame = new AuthenticationForm(this);
		result = false;
		retriesLeft = 0;
	}

	/**
	 * Authenticates the user by prompting an authentication dialog. This method
	 * blocks until user was authenticated successfully.
	 * 
	 * @param maxRetries
	 *            maximum number of retries until false is returned. 0 for no
	 *            retries.
	 * 
	 * @return true if user is authenticated successfully, false otherwise.
	 */
	public boolean authenticate(int maxRetries) {
		if (maxRetries < 0)
			maxRetries = 0;
		retriesLeft = maxRetries;
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
		if (success || retriesLeft <= 0)
			frame.hide();
		else {
			retriesLeft--;
			frame.onLoginFailed();
		}
	}

	void authenticate(String username, char[] password) {
		frame.onLoginStart();
		worker = new AuthenticationWorker(username, password.toString());
		worker.addPropertyChangeListener(new AuthListener());
		worker.execute();
	}
}
