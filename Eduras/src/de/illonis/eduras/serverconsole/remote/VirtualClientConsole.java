package de.illonis.eduras.serverconsole.remote;

import de.eduras.remote.RemoteException;
import de.illonis.eduras.serverconsole.ConsolePrinter;

/**
 * Represents a client console on server side.
 * 
 * @author illonis
 * 
 */
public class VirtualClientConsole implements ConsolePrinter {
	private final RemoteConsoleServer remoteConsoleServer;
	private final int clientId;

	VirtualClientConsole(int id, RemoteConsoleServer remoteConsoleServer) {
		this.clientId = id;
		this.remoteConsoleServer = remoteConsoleServer;
	}

	@Override
	public void println(String line) {
		try {
			remoteConsoleServer.answer(clientId, line);
		} catch (RemoteException e) {
			remoteConsoleServer.removeClient(clientId);
		}
	}

	@Override
	public void printlnf(String line, Object... args) {
		println(String.format(line, args));
	}
}
