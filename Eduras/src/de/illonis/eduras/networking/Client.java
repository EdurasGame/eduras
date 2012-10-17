package de.illonis.eduras.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import de.illonis.eduras.ClientFrame;
import de.illonis.eduras.Game;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.Logic;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * A client that connects to the game server and starts receiving and sending
 * events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Client implements GameEventListener {

	Socket socket;
	private final Logic logic;

	ClientReceiver receiver;
	ClientSender sender;
	private final Game game;
	private final ClientFrame clientFrame;

	/**
	 * Creates a new Client.
	 */
	public Client() {
		game = new Game();
		GameObject obj = new GameObject();
		game.setPlayer1(obj);
		this.logic = new Logic(game);
		logic.addGameEventListener(this);
		clientFrame = new ClientFrame(this);
		sender = new ClientSender(socket);
		clientFrame.setVisible(true);
	}

	/**
	 * Returns current game
	 * 
	 * @return current game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Connect to a server at given address using default port (
	 * {@value Server#DEFAULT_PORT}).
	 * 
	 * @param addr
	 *            server-address
	 */
	public void connect(InetAddress addr) {
		connect(addr, Server.DEFAULT_PORT);
	}

	/**
	 * Connects to a server on the given address and port.
	 * @param addr The server's address.
	 * @param port The server's port.
	 */
	public void connect(InetAddress addr, int port) {
		try {
			System.out.println("[CLIENT] Connecting...");
			socket = new Socket(addr, port);
			receiver = new ClientReceiver(logic, socket);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sends a message to Server
	 * 
	 * @param message
	 *            message to send
	 */
	public void sendMessage(String message) {
		sender.sendMessage(message);

	}

	@Override
	public void onWorldChanged() {
		clientFrame.newCircle(game.getPlayer1().getXPosition(), game
				.getPlayer1().getYPosition());
	}
}
