package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.EdurasGameInterface;
import de.illonis.eduras.gameclient.LoginData;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;

/**
 * Controls the connection progress.
 * 
 * @author illonis
 * 
 */
public class ConnectingController extends EdurasScreenController {

	private final static Logger L = EduLog
			.getLoggerFor(ConnectingController.class.getName());

	private ConnectionEstablisher connectCallable;
	private Future<Boolean> connectFuture = null;
	private boolean connect = false;
	private final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(
			2);

	private Label note;

	ConnectingController(GameControllerBridge game) {
		super(game);
	}

	/**
	 * @param connect
	 *            true if connecting should start.
	 */
	public void setConnect(boolean connect) {
		this.connect = connect;
	}

	@Override
	protected void initScreen(Screen screen) {
		note = screen.findNiftyControl("noteLabel", Label.class);
	}

	/**
	 * Aborts connection progress.
	 */
	public void abort() {
		connect = false;
		if (connectFuture != null)
			connectFuture.cancel(true);
		game.enterState(2);
	}

	/**
	 * logic update
	 */
	public void update() {
		if (connect) {
			if (connectFuture == null) {
				// if we have not started loading yet, submit the Callable to
				// the executor
				game.initClient();
				EdurasGameInterface eduras = game.getEduras();
				LoginData data = game.getLoginData();
				eduras.setClientName(data.getUsername());
				eduras.setRole(data.getRole());
				connectCallable = new ConnectionEstablisher(data);
				connectFuture = exec.submit(connectCallable);
			}
			// check if the execution on the other thread is done
			if (connectFuture.isDone()) {
				// these calls have to be done on the update loop thread,
				// especially attaching the terrain to the rootNode
				// after it is attached, it's managed by the update loop thread
				// and may not be modified from any other thread anymore!

				connect = false;
				boolean result;
				try {
					result = connectFuture.get();
					if (result) {
						game.enterState(3);
					} else {
						note.setText(connectCallable.getErrorMessage());
					}
				} catch (InterruptedException | ExecutionException e) {
					note.setText(e.getMessage());
				}
				connectFuture = null;
			}
		}
	}

	class ConnectionEstablisher implements Callable<Boolean> {
		private final LoginData data;
		private String errorMessage;

		public ConnectionEstablisher(LoginData data) {
			this.data = data;
		}

		@Override
		public Boolean call() throws Exception {
			errorMessage = "";
			try {
				game.getEduras().connect(data.getAddress(), data.getPort());
				return true;
			} catch (SocketTimeoutException e) {
				errorMessage = "Connection timeout";
			} catch (IOException e) {
				errorMessage = e.getMessage();
				L.log(Level.SEVERE, "could not connect", e);
			} finally {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					L.log(Level.WARNING, "interrupted", e);
				}
			}
			return false;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

	}
}
