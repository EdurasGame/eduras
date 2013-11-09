package de.illonis.eduras;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.logic.ServerEventTriggerer;
import de.illonis.eduras.logic.ServerLogic;
import de.illonis.eduras.maps.FunMap;
import de.illonis.eduras.networking.Server;
import de.illonis.eduras.networking.discover.MetaServer;
import de.illonis.eduras.networking.discover.ServerDiscoveryListener;
import de.illonis.eduras.networking.discover.ServerSearcher;
import de.illonis.eduras.serverconsole.NoConsoleException;
import de.illonis.eduras.serverconsole.ServerConsole;
import de.illonis.eduras.serverconsole.commands.CommandInitializer;

/**
 * Used to start eduras?-server via main method.
 * 
 * @author illonis
 * 
 */
public class Eduras {
	private final static Logger L = EduLog.getLoggerFor(Eduras.class.getName());

	/**
	 * Starts an Eduras? server.
	 * 
	 * @param args
	 *            Arguments passed from console.
	 *            <ul>
	 *            <li><b>arg0:</b> custom server name.</li>
	 *            <li><b>arg1:</b> custom port to listen on.</li>
	 *            </ul>
	 */
	public static void main(String[] args) {

		try {
			EduLog.init("server.log");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// EduLog.setBasicLogLimit(Level.WARNING);
		// EduLog.setConsoleLogLimit(Level.WARNING);

		int port = Server.DEFAULT_PORT;
		String name = Server.DEFAULT_NAME;

		if (args.length > 0) {
			name = args[0];
			if (args.length > 1) {
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					L.severe(Localization.getStringF("Server.invalidportarg",
							args[1]));
					return;
				}
			}
		}

		L.info(Localization.getString("Server.startstart"));

		L.info("Preloading shapes...");
		GraphicsPreLoader.preLoadShapes();
		L.info("Shape caching finished.");

		Server server = new Server(port, name);

		GameInformation gameInfo = new GameInformation();
		ServerLogic logic = new ServerLogic(gameInfo);

		ServerEventTriggerer eventTriggerer = new ServerEventTriggerer(logic);

		eventTriggerer.setServerSender(server.getServerSender());
		gameInfo.setEventTriggerer(eventTriggerer);

		server.setGame(logic.getGame());
		server.setLogic(logic, server);

		eventTriggerer.changeMap(new FunMap());

		try {
			server.start();
		} catch (ServerNotReadyForStartException e) {
			L.severe(Localization.getStringF("Server.notready", e.getMessage()));
			return;
		}

		getInterfaces();

		try {
			ServerConsole.start();
			CommandInitializer.initCommands();
			ServerConsole.setEventTriggerer(new ConsoleEventTriggerer(
					eventTriggerer, server));
		} catch (NoConsoleException e) {
			L.log(Level.WARNING, "Could not find console", e);
		}

		ServerDiscoveryListener sdl = new ServerDiscoveryListener(
				server.getName(), port);
		sdl.start();

		if (args.length > 2 && args[2].equals("-m"))
			registerAtMetaServer();
	}

	private static void registerAtMetaServer() {
		Socket socket;
		try {
			socket = new Socket(ServerSearcher.METASERVER_ADDRESS,
					ServerDiscoveryListener.META_SERVER_PORT);
			new PrintWriter(socket.getOutputStream(), true)
					.println(MetaServer.REGISTER_REQUEST);
		} catch (IOException e) {
			L.warning("Cannot connect to meta server.");
		}
	}

	/**
	 * Lists all IPv4 addresses server is reachable at.
	 */
	public static void getInterfaces() {

		ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface
					.getNetworkInterfaces();

			while (e.hasMoreElements()) {

				NetworkInterface ni = e.nextElement();

				Enumeration<InetAddress> e2 = ni.getInetAddresses();

				while (e2.hasMoreElements()) {
					InetAddress ip = e2.nextElement();
					if (ip instanceof Inet4Address)
						addresses.add(ip);
				}
			}
		} catch (SocketException e) {
			L.log(Level.SEVERE, Localization.getString("Server.noaddresses"), e);
			return;
		}

		StringBuilder b = new StringBuilder(
				Localization.getString("Server.reachable") + " ");
		for (InetAddress inetAddress : addresses) {
			b.append(inetAddress.toString().substring(1));
			b.append(", ");
		}
		L.info(b.toString());
	}
}
