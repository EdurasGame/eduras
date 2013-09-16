package de.illonis.eduras;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;

import de.illonis.eduras.exceptions.ServerNotReadyForStartException;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logger.EduLog.LogMode;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.logic.ServerEventTriggerer;
import de.illonis.eduras.logic.ServerLogic;
import de.illonis.eduras.maps.FunMap;
import de.illonis.eduras.networking.Server;
import de.illonis.eduras.networking.discover.ServerDiscoveryListener;
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
		// new LoggerGui().setVisible(true);
		EduLog.setLogOutput(LogMode.CONSOLE);
		EduLog.setLogLimit(Level.WARNING);
		int port = Server.DEFAULT_PORT;
		String name = Server.DEFAULT_NAME;

		if (args.length > 0) {
			name = args[0];
			if (args.length > 1) {
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					EduLog.error(Localization.getStringF(
							"Server.invalidportarg", args[1]));
					return;
				}
			}
		}

		EduLog.info(Localization.getString("Server.startstart"));

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
			EduLog.error(Localization.getStringF("Server.notready",
					e.getMessage()));
			return;
		}

		getInterfaces();

		try {
			ServerConsole.start();
			CommandInitializer.initCommands();
			ServerConsole.setEventTriggerer(new ConsoleEventTriggerer(
					eventTriggerer, server));
		} catch (NoConsoleException e1) {
			EduLog.passException(e1);
		}

		ServerDiscoveryListener sdl = new ServerDiscoveryListener(
				server.getName(), port);
		sdl.start();

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
			EduLog.error(Localization.getString("Server.noaddresses"));
			EduLog.passException(e);
			return;
		}

		StringBuilder b = new StringBuilder(
				Localization.getString("Server.reachable") + " ");
		for (InetAddress inetAddress : addresses) {
			b.append(inetAddress.toString().substring(1));
			b.append(", ");
		}
		EduLog.info(b.toString());
	}
}
