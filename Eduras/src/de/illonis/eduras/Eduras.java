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
		new Server();
		new ClientFrame();
	}
}