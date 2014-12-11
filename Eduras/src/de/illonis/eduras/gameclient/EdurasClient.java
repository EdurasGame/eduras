package de.illonis.eduras.gameclient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.SysOutCatcher;
import de.illonis.eduras.exceptions.EdurasUncaughtExceptionHandler;
import de.illonis.eduras.gameclient.gui.hud.nifty.EdurasSlickClient;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.utils.Pair;
import de.illonis.eduras.utils.ReflectionTools;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

/**
 * Eduras? Game client for end user.
 * 
 * @author illonis
 * 
 */
public class EdurasClient {

	private final static Logger L = EduLog.getLoggerFor(EdurasClient.class
			.getName());

	/**
	 * Indicates how long an Eduras client tries to connect to a server.
	 */
	public static final int CONNECT_TIMEOUT = 10000;

	private static final Level DEFAULT_LOGLIMIT = Level.WARNING;

	private static int PORT = 4386;

	/**
	 * Starts Eduras? client.
	 * 
	 * @param args
	 *            First argument is considered to be the port to which the
	 *            client is bound.
	 */
	public static void main(String[] args) {

		SimpleDateFormat simpleDate = new SimpleDateFormat("y-M-d-H-m-s");

		try {
			EduLog.init(new File("logs").getAbsolutePath(),
					simpleDate.format(new Date()) + "-client.log", 2097152);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread.setDefaultUncaughtExceptionHandler(new EdurasUncaughtExceptionHandler(
				L));

		// arguments are of form <parametername>=<parametervalue>
		List<Pair<String, String>> parameters = new LinkedList<Pair<String, String>>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("=")) {
				String[] details = args[i].split("=");
				parameters
						.add(new Pair<String, String>(details[0], details[1]));
			}
		}

		String betaUser = "";
		String betaPassword = "";

		String serverIp = "";
		int serverPort = 0;

		final String sClassName = S.class.getSimpleName();

		// read arguments

		Level logLimit = DEFAULT_LOGLIMIT;
		for (Pair<String, String> pair : parameters) {
			String parameterName = pair.getFirst();
			String parameterValue = pair.getSecond();

			if (parameterName.startsWith(sClassName + ".")) {
				try {
					Field f = S.Client.class.getField(parameterName
							.substring(sClassName.length() + 1));
					Class<?> targetClass = f.getType();
					Object value = ReflectionTools.toPrimitive(targetClass,
							parameterValue);
					f.set(null, value);
					L.log(Level.INFO, "Set S." + f.getName() + " to " + value);
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException e) {
					L.log(Level.WARNING,
							"Failed to set S field from command line, argument: "
									+ parameterName + "=" + parameterValue, e);
				}
				continue;
			}

			switch (parameterName.toLowerCase()) {
			case "port":
				try {
					PORT = Integer.parseInt(parameterValue);
					if (PORT < 1024 || PORT > 49151) {
						throw new Exception();
					}
				} catch (Exception e) {
					L.severe("Given port is not a valid value!");
					return;
				}
				break;
			case "resfolder":
				Path path = Paths.get(parameterValue);
				if (!Files.exists(path) || !Files.isDirectory(path)) {
					L.log(Level.SEVERE,
							"Custom resource folder does not exist or is not a directory: "
									+ path.toString() + ". Using default one");
				} else {
					S.resource_folder = parameterValue;
					L.log(Level.WARNING, "Using custom folder for resources: "
							+ path.toString());
				}
				break;
			case "betauser":
				betaUser = parameterValue;
				break;
			case "betapassword":
				betaPassword = parameterValue;
				break;
			case "loglimit":
				logLimit = Level.parse(parameterValue);
				break;
			case "debug":
				S.fromEclipse = true;
				break;
			case "serverip":
				serverIp = parameterValue;
				break;
			case "serverport":
				try {
					serverPort = Integer.parseInt(parameterValue);
					if (serverPort < 1024 || serverPort > 49151) {
						throw new Exception();
					}
				} catch (Exception e) {
					L.severe("Given port is not a valid value!");
					return;
				}
				break;
			default:
				L.warning("Unknown parametername '" + parameterName + "'");
				continue;
			}
		}
		EduLog.setBasicLogLimit(logLimit);
		EduLog.setConsoleLogLimit(logLimit);
		EduLog.setFileLogLimit(logLimit);

		if (S.Client.exit_on_sysout) {
			SysOutCatcher.startCatching();
		}

		try {
			ResourceManager.extractMaps();
		} catch (IOException e) {
			L.log(Level.SEVERE, "Can not create data folder.");
		}

		if (!S.fromEclipse)
			System.setProperty("org.lwjgl.librarypath", ResourceManager
					.resourceToPath(ResourceType.NATIVE_LIBRARY, "").toString());
		EdurasSlickClient client = new EdurasSlickClient();
		try {
			client.startGui(betaUser, betaPassword, serverIp, serverPort);
		} catch (SlickException e) {
			L.log(Level.SEVERE, "Slick error at startup", e);
		}
	}
}
