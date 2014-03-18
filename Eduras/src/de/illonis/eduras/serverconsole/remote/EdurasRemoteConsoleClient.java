package de.illonis.eduras.serverconsole.remote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import de.eduras.remote.EncryptedRemoteClient;
import de.eduras.remote.RemoteException;
import de.eduras.remote.RemoteServer;
import de.eduras.remote.security.EncryptionService;
import de.illonis.edulog.EduLog;

public class EdurasRemoteConsoleClient extends EncryptedRemoteClient {

	private final static Logger L = EduLog
			.getLoggerFor(EdurasRemoteConsoleClient.class.getName());

	private final boolean authenticated;
	private final BufferedReader console;
	private final RemoteConsoleClientFrame frame;

	public EdurasRemoteConsoleClient() {
		authenticated = false;
		frame = new RemoteConsoleClientFrame(this);
		console = new BufferedReader(new InputStreamReader(System.in));
	}

	public void showGui() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				frame.show();
			}
		}).start();
		frame.appendOutput("Waiting for connection details...");
	}

	public void askConnection() {
		String s = (String) JOptionPane.showInputDialog(null,
				"Enter server address:\n" + "\"e.g. 192.168.0.4:4441",
				"Connect to remote console", JOptionPane.PLAIN_MESSAGE, null,
				null, "localhost:" + RemoteServer.DEFAULT_REMOTE_SERVER_PORT);

		// If a string was returned, say so.
		if ((s == null) || (s.length() <= 0)) {
			System.exit(0);
			return;
		}

		String[] vals = s.split(":");
		InetAddress addr;
		try {
			addr = InetAddress.getByName(vals[0]);
			int port = Integer.parseInt(vals[1]);
			frame.appendOutput("Connecting...");
			if (connect(addr, port)) {
				frame.appendOutput("Connected.");
				frame.appendOutput("Enter password:");
				frame.ready();
			} else {
				frame.appendOutput("Connecting failed.");
			}
		} catch (UnknownHostException | NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid address entered.",
					"Error", JOptionPane.ERROR_MESSAGE);
			L.log(Level.WARNING, "Invalid address entered.", e);
		}

	}

	public static void main(String[] args) {
		EdurasRemoteConsoleClient client = new EdurasRemoteConsoleClient();
		client.showGui();
		client.askConnection();
	}

	@Override
	public void onAuthenticationSuccessed() {
		frame.appendOutput("Authentication successed.");
	}

	@Override
	public void onAuthenticationFailed() {
		frame.appendOutput("Auth failed.");
		frame.appendOutput("Enter password:");
	}

	@Override
	public void onAnswer(String answer) {
		frame.appendOutput(answer);
	}

	@Override
	public void onDisconnected() {
		frame.appendOutput("disconnected");
		System.exit(0);
	}

	@Override
	public void onConnectionLost() {
		frame.appendOutput("connection lost");
		System.exit(1);
	}

	public void onCommand(String command) throws RemoteException {
		if (command.equals("exit")) {
			disconnect();
			return;
		}
		if (!isAuthenticated()) {
			// if we are prompting for a password, we must encrypt it!
			try {
				command = EncryptionService.oneWayEncrypt(command);
			} catch (UnsupportedEncodingException e) {
				L.log(Level.SEVERE, "Encryption not supported.", e);
				return;
			}
		}
		sendCommand(command);
	}

	@Override
	public void onConnectionEstablished(int clientId) {
		frame.appendOutput("Connection established.");
	}
}
