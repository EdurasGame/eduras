package de.illonis.eduras;

import de.illonis.eduras.networking.Client;
import de.illonis.eduras.networking.Server;

public class Eduras {

	/**
	 * @param args
	 * 
	 *            If <b>first argument</b> equals "server", server will be
	 *            started, client otherwise.
	 * 
	 */
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("server")) {
			System.out.println("Starting Eduras? server...");
			new Server();
		} else {
			System.out.println("Starting Eduras? client...");
			new Client();

		}
	}
}