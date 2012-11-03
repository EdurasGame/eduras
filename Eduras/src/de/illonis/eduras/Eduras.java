package de.illonis.eduras;

import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.networking.Server;

public class Eduras {

	/**
	 * @param args
	 *            Arguments passed from console.
	 *            <ul>
	 *            <li><b>arg0:</b> if equals "server", server will be started,
	 *            client otherwise.</li>
	 *            </ul>
	 */
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("server")) {
			System.out.println("Starting Eduras? server...");
			GameInformation gameInfo = new GameInformation();
			Logic logic = new Logic(gameInfo);
			Server server;
			if (args.length > 1) {
				int port = Integer.parseInt(args[1]);
				server = new Server(port);
			} else
				server = new Server();

			server.setGame(logic.getGame());
			server.setLogic(logic, new NetworkEventListener() {

				@Override
				public void onNetworkEventAppeared(NetworkEvent event) {
					// do nothin
				}

			});
			try {
				server.start();
			} catch (ServerNotReadyForStartException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: allow specifying a custom server url
			System.out.println("Starting Eduras? client...");
		}
	}
}
