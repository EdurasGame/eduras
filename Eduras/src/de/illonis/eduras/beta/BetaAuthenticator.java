package de.illonis.eduras.beta;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class BetaAuthenticator implements AuthGuiHandler {

	private final AuthenticationForm frame;
	private AuthenticationWorker worker;
	private boolean result;

	public BetaAuthenticator() {
		frame = new AuthenticationForm(this);
		result = false;
	}

	public static void main(String[] args) {
		BetaAuthenticator auth = new BetaAuthenticator();
		System.out.println(auth.authenticate());
	}

	public boolean authenticate() {
		frame.show();
		System.out.println("hidden");
		return result;
	}

	class AuthListener implements PropertyChangeListener {
		private final AuthenticationForm frame;

		public AuthListener(AuthenticationForm frame) {
			this.frame = frame;
		}

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
		if (success)
			frame.hide();
		else
			frame.onLoginFailed();
	}

	@Override
	public void authenticate(String username, char[] password) {
		frame.onLoginStart();
		worker = new AuthenticationWorker(username, password.toString());
		worker.addPropertyChangeListener(new AuthListener(frame));
		worker.execute();
	}

}
