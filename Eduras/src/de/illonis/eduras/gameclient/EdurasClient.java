package de.illonis.eduras.gameclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.SysOutCatcher;
import de.illonis.eduras.beta.BetaAuthenticator;
import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.FullScreenClientFrame;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.utils.PathFinder;
import de.illonis.eduras.utils.ReflectionTools;

/**
 * Eduras? Game client for end user.
 * 
 * @author illonis
 * 
 */
public class EdurasClient {

	private final static Logger L = EduLog.getLoggerFor(EdurasClient.class
			.getName());

	private final static String[] nativeFiles = new String[] {
			"jinput-dx8_64.dll", "jinput-dx8.dll", "jinput-raw_64.dll",
			"jinput-raw.dll", "libjinput-linux.so", "libjinput-linux64.so",
			"libjinput-osx.jnilib", "liblwjgl.jnilib", "liblwjgl.so",
			"liblwjgl64.so", "libopenal.so", "libopenal64.so", "lwjgl.dll",
			"lwjgl64.dll", "openal.dylib", "OpenAL32.dll", "OpenAL64.dll" };

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
			EduLog.init(simpleDate.format(new Date()) + "-client.log", 2097152);
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean debug = false;
		// arguments are of form <parametername>=<parametervalue>
		String[][] parametersWithValues = new String[args.length][2];

		for (int i = 0; i < args.length; i++) {
			parametersWithValues[i] = args[i].split("=");
		}

		String betaUser = "";
		String betaPassword = "";

		final String sClassName = S.class.getSimpleName();

		// read arguments
		Level logLimit = DEFAULT_LOGLIMIT;
		for (int i = 0; i < args.length; i++) {

			String parameterName = parametersWithValues[i][0];
			String parameterValue = parametersWithValues[i][1];

			if (parameterName.equalsIgnoreCase("port")) {
				try {
					PORT = Integer.parseInt(parameterValue);
					if (PORT < 1024 || PORT > 49151) {
						throw new Exception();
					}
				} catch (Exception e) {
					L.severe("Given port is not a valid value!");
					return;
				}
			} else if (parameterName.equalsIgnoreCase("betaUser")) {
				betaUser = parameterValue;
			} else if (parameterName.equalsIgnoreCase("betaPassword")) {
				betaPassword = parameterValue;
			} else if (parameterName.equalsIgnoreCase("loglimit")) {
				logLimit = Level.parse(parameterValue);
			} else if (parameterName.equalsIgnoreCase("debug")) {
				debug = true;
			} else if (parameterName.startsWith(sClassName + ".")) {
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
			}
		}
		EduLog.setBasicLogLimit(logLimit);
		EduLog.setConsoleLogLimit(logLimit);
		EduLog.setFileLogLimit(logLimit);

		if (S.Client.exit_on_sysout) {
			SysOutCatcher.startCatching();
		}

		try {
			extractNatives();
		} catch (UnsatisfiedLinkError | IOException e) {
			L.log(Level.SEVERE, "Could not extract native libraries.", e);
		}

		// Note that this is very bad coded due to testing ;)
		// buildChooserFrame();

		if (!debug)
			System.setProperty("org.lwjgl.librarypath",
					(new File(PathFinder.findFile("native"))).getAbsolutePath());

		BetaAuthenticator authenticator = new BetaAuthenticator();
		// if (authenticator.authenticate(3, betaUser, betaPassword)) {
		startWindowed();
		// }
	}

	private static void extractNatives() throws UnsatisfiedLinkError,
			IOException {
		URI nativeDir = PathFinder.findFile("native/");
		Path nativePath = Paths.get(nativeDir);
		if (Files.exists(nativePath, LinkOption.NOFOLLOW_LINKS)) {
			L.info("Found native folder. Skipping extraction.");
			return;
		} else {
			L.fine("Creating native directory at " + nativePath);
			Files.createDirectory(nativePath);
		}

		L.info("Extracting native libraries...");
		for (String file : nativeFiles) {
			InputStream internalFile = EdurasClient.class
					.getResourceAsStream("/native/" + file);
			if (internalFile == null)
				throw new UnsatisfiedLinkError("Could not load " + file);

			Path target = Paths.get(nativeDir.resolve(file));
			Files.copy(internalFile, target,
					StandardCopyOption.REPLACE_EXISTING);
		}

		L.info("Done extracting native libraries.");
	}

	protected static void startWindowed() {
		GameClient client = new GameClient();
		ClientFrame f = new ClientFrame(client);
		client.useFrame(f);
		client.startGui();
	}

	protected static void startFullScreen(DisplayMode displayMode) {
		GameClient client = new GameClient();

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		ClientFrame f = new FullScreenClientFrame(
				graphicsEnvironment.getDefaultScreenDevice(), displayMode,
				client);

		client.useFrame(f);
		client.startGui();
	}
}
