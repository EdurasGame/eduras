package de.illonis.eduras.gameclient;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.logicabstraction.NetworkManager;

public class ConnectionEstablisher extends Thread {

	private final static Logger L = EduLog
			.getLoggerFor(ConnectionEstablisher.class.getName());

	private final NetworkManager nwm;
	private final LoginData data;
	private String errorMessage;

	public ConnectionEstablisher(LoginData data, NetworkManager nwm) {
		super("ConnectionEstablisher");
		this.nwm = nwm;
		this.data = data;
	}

	@Override
	public void run() {
		errorMessage = "";
		try {
			nwm.connect(data.getAddress(), data.getPort());
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
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
