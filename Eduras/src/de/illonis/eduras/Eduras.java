package de.illonis.eduras;

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
			new Server();
		} else {
			// TODO: allow specifying a custom server url
			System.out.println("Starting Eduras? client...");
		}
	}
}