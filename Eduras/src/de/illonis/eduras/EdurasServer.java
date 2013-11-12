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

import de.eduras.eventingserver.Server;
import de.eduras.eventingserver.ServerInterface;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.logic.ServerEventTriggerer;
import de.illonis.eduras.logic.ServerLogic;
import de.illonis.eduras.maps.FunMap;
import de.illonis.eduras.networking.EventParser;
import de.illonis.eduras.networking.InetPolizei;
import de.illonis.eduras.networking.ServerNetworker;
import de.illonis.eduras.networking.discover.MetaServer;
import de.illonis.eduras.networking.discover.ServerDiscoveryListener;
import de.illonis.eduras.networking.discover.ServerSearcher;
import de.illonis.eduras.serverconsole.NoConsoleException;
import de.illonis.eduras.serverconsole.ServerConsole;
import de.illonis.eduras.serverconsole.commands.CommandInitializer;

/**
 * The eduras server main executable.
 * 
 * @author illonis
 * 
 */
public class EdurasServer {

	private final static int DEFAULT_PORT = 4387;
	private final static String DEFAULT_NAME = "Eduras Server";
	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

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

		int port = DEFAULT_PORT;
		String name = DEFAULT_NAME;

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

		L.info("Caching shapes...");
		GraphicsPreLoader.preLoadShapes();
		L.info("Caching shapes done.");

		L.info(Localization.getString("Server.startstart"));

		ServerInterface server = new Server();

		GameInformation gameInfo = new GameInformation();
		ServerLogic logic = new ServerLogic(gameInfo);

		ServerEventTriggerer eventTriggerer = new ServerEventTriggerer(logic,
				server);

		gameInfo.setEventTriggerer(eventTriggerer);

		server.setEventHandler(new EventParser(logic));
		server.setNetworkEventHandler(new ServerNetworker());
		server.setPolicy(new InetPolizei());

		eventTriggerer.changeMap(new FunMap());

		server.start(name, port);

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
