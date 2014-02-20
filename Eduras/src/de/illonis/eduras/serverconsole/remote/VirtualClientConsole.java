package de.illonis.eduras.serverconsole.remote;

import de.eduras.remote.RemoteException;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

public class VirtualClientConsole implements ConsolePrinter {
	private final ConsoleEventTriggerer triggerer;
	private final RemoteConsoleServer remoteConsoleServer;
	private final int id;

	public VirtualClientConsole(int id,
			RemoteConsoleServer remoteConsoleServer,
			ConsoleEventTriggerer triggerer) {
		this.id = id;
		this.triggerer = triggerer;
		this.remoteConsoleServer = remoteConsoleServer;
	}

	@Override
	public void println(String line) {
		try {
			remoteConsoleServer.answer(id, line);
		} catch (RemoteException e) {
			remoteConsoleServer.removeClient(id);
		}
	}

	@Override
	public void printlnf(String line, Object... args) {
		println(String.format(line, args));
	}
}
