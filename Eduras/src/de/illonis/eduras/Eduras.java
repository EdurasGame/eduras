package de.illonis.eduras;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.networking.Server;

/**
 * Used to start eduras?-server via main method.
 * 
 * @author illonis
 * 
 */
public class Eduras {

	/**
	 * Starts an Eduras? server.
	 * 
	 * @param args
	 *            Arguments passed from console.
	 *            <ul>
	 *            <li><b>arg0:</b> custom port to listen on.
	 *            </ul>
	 */
	public static void main(String[] args) {
		int port = 0;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("[ERROR] Invalid custom port: " + args[0]);
				System.out.println("Quitting.");
				return;
			}
		}

		System.out.println("Starting Eduras? server...");

		Server server;
		if (port > 0) {
			server = new Server(port);
		} else {
			server = new Server();
		}

		GameInformation gameInfo = new GameInformation();
		Logic logic = new Logic(gameInfo);

		server.setGame(logic.getGame());
		server.setLogic(logic, new NetworkEventListener() {
			@Override
			public void onNetworkEventAppeared(NetworkEvent event) {
				// do nothing
			}
		});

		try {
			server.start();
		} catch (ServerNotReadyForStartException e) {
			System.out.println("Server could not start: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		getInterfaces();

	}

	/**
	 * Lists all IPv4 addresses server is reachable on.
	 */
	public static void getInterfaces() {

		ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {

				NetworkInterface ni = (NetworkInterface) e.nextElement();

				Enumeration<InetAddress> e2 = ni.getInetAddresses();

				while (e2.hasMoreElements()) {
					InetAddress ip = (InetAddress) e2.nextElement();
					if (ip instanceof Inet4Address)
						addresses.add(ip);
				}
			}
		} catch (SocketException e) {
			System.out.println("[ERROR] Could not determine network addresses.");
			e.printStackTrace();
			return;
		}
		System.out.println("[STARTUP] Server is reachable on:");
		for (InetAddress inetAddress : addresses) {
			System.out.println("[STARTUP] " + inetAddress.toString().substring(1));
		}
	}
}
