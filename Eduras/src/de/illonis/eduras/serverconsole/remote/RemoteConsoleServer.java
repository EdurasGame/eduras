package de.illonis.eduras.serverconsole.remote;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import de.eduras.remote.EncryptedRemoteServer;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.CommandInput;
import de.illonis.eduras.serverconsole.InvalidCommandException;
import de.illonis.eduras.serverconsole.ServerConsole;

public class RemoteConsoleServer extends EncryptedRemoteServer {

	private final static String PROMPT = "eduras-remote>";
	private final static String EXIT_COMMAND = "exit";
	private int lastClient = 0;
	private final HashMap<Integer, VirtualClientConsole> consoles;
	private final ConsoleEventTriggerer triggerer;
	private final ServerConsole console;

	String buffer;

	public RemoteConsoleServer(ServerConsole c,
			ConsoleEventTriggerer triggerer, String password)
			throws UnsupportedEncodingException {
		super(password);
		this.console = c;
		this.triggerer = triggerer;
		consoles = new HashMap<Integer, VirtualClientConsole>();
	}

	void addClient(int clientId) {
		System.out.println("addclient" + clientId);
		consoles.put(clientId, new VirtualClientConsole(clientId, this,
				triggerer));
	}

	void removeClient(int clientId) {
		consoles.remove(clientId);
	}

	@Override
	public void onCommand(int client, String command) {
		System.out.println("received " + command + " from client " + client);
		VirtualClientConsole clientConsole = consoles.get(client);

		if (command.equals(EXIT_COMMAND)) {
			clientConsole.println("Bye.");
			removeClient(client);
		}
		try {
			CommandInput input = console.parseCommand(command);
			input.getCommand().onCommand(input.getArgs(), clientConsole,
					triggerer);
		} catch (InvalidCommandException e) {
			clientConsole.println(e.getMessage());
		}
	}
}
