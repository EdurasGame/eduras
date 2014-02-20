package de.illonis.eduras.serverconsole.remote;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import de.eduras.remote.EncryptedRemoteServer;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.CommandInput;
import de.illonis.eduras.serverconsole.CommandParser;
import de.illonis.eduras.serverconsole.InvalidCommandException;

/**
 * A remote server accepting clients that use server console.
 * 
 * @author illonis
 * 
 */
public class RemoteConsoleServer extends EncryptedRemoteServer {

	private final static String EXIT_COMMAND = "exit";
	private final HashMap<Integer, VirtualClientConsole> consoles;
	private final ConsoleEventTriggerer triggerer;
	private final CommandParser parser;

	String buffer;

	/**
	 * @param parser
	 *            the parser that parses commands.
	 * @param triggerer
	 *            the triggerer that performs command actions.
	 * @param password
	 *            the password used for authentication.
	 * @throws UnsupportedEncodingException
	 *             if UTF-8 is not supported.
	 */
	public RemoteConsoleServer(CommandParser parser,
			ConsoleEventTriggerer triggerer, String password)
			throws UnsupportedEncodingException {
		super(password);
		this.parser = parser;
		this.triggerer = triggerer;
		consoles = new HashMap<Integer, VirtualClientConsole>();
	}

	void addClient(int clientId) {
		consoles.put(clientId, new VirtualClientConsole(clientId, this));
	}

	void removeClient(int clientId) {
		consoles.remove(clientId);
	}

	@Override
	public void onCommand(int client, String command) {
		VirtualClientConsole clientConsole = consoles.get(client);

		if (command.equals(EXIT_COMMAND)) {
			clientConsole.println("Bye.");
			removeClient(client);
		}
		try {
			CommandInput input = parser.parseCommand(command);
			input.getCommand().onCommand(input.getArgs(), clientConsole,
					triggerer);
		} catch (InvalidCommandException e) {
			clientConsole.println(e.getMessage());
		}
	}
}
