package de.illonis.eduras.serverconsole;

import java.io.UnsupportedEncodingException;

import de.eduras.remote.EncryptedRemoteServer;
import de.eduras.remote.RemoteException;

public class RemoteConsoleServer extends EncryptedRemoteServer implements
		ConsoleInterface {

	private final static String PROMPT = "eduras-remote>";
	private int lastClient = 0;

	String buffer;

	public RemoteConsoleServer(ServerConsole c, String password)
			throws UnsupportedEncodingException {
		super(password);

		c.setConsole(this);
	}

	@Override
	public void onCommand(int client, String command) {
		lastClient = client;
		bufferCommand(client, command);

	}

	private void bufferCommand(int client, String command) {
		buffer = command;
		// TODO Auto-generated method stub
	}

	@Override
	public void writeLine(String line) {
		try {
			answer(lastClient, line);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void writef(String str, Object[] args) {
		try {
			answer(lastClient, String.format(str, args));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String readLine() {
		try {
			answer(lastClient, PROMPT);
			return getfromBuffer(lastClient);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<error>";
	}

	private String getfromBuffer(int client) {
		return buffer;
	}

	@Override
	public String readLine(String prompt) {
		try {
			answer(lastClient, prompt);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<error>";
	}

}
