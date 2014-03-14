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
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.IOException;
import java.lang.reflect.Field;
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
import de.illonis.eduras.beta.BetaAuthenticator;
import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.FullScreenClientFrame;
import de.illonis.eduras.settings.S;

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
			EduLog.init(simpleDate.format(new Date()) + "-client.log", 2097152);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			} else if (parameterName.startsWith(sClassName + ".")) {
				try {
					Field f = S.class.getField(parameterName
							.substring(sClassName.length() + 1));
					Class<?> targetClass = f.getType();
					Object value = convert(targetClass, parameterValue);
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

		// Note that this is very bad coded due to testing ;)
		// buildChooserFrame();

		BetaAuthenticator authenticator = new BetaAuthenticator();
		if (authenticator.authenticate(3, betaUser, betaPassword)) {
			startWindowed();
		}
	}

	private static Object convert(Class<?> targetType, String text) {
		PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
		editor.setAsText(text);
		return editor.getValue();
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

	/**
	 * Shows a very bad coded frame that allows user to choose between
	 * fullscreen or windowed mode.
	 */
	private static void buildChooserFrame() {
		final JFrame cf = new JFrame("Eduras? client");
		JPanel panel = new JPanel(new BorderLayout());
		final JRadioButton windowedRadio = new JRadioButton("Window mode");

		final JRadioButton fullscreenRadio = new JRadioButton(
				"Full screen mode");
		ButtonGroup group = new ButtonGroup();
		group.add(windowedRadio);
		group.add(fullscreenRadio);
		windowedRadio.setSelected(true);
		JPanel windowPanel = new JPanel();
		windowPanel.add(windowedRadio);
		JPanel fullPanel = new JPanel(new BorderLayout());
		fullPanel.add(fullscreenRadio, BorderLayout.NORTH);

		panel.add(windowPanel, BorderLayout.NORTH);
		panel.add(fullPanel, BorderLayout.CENTER);

		final JButton okButton = new JButton("Start");

		JLabel currentRes = new JLabel();
		fullPanel.add(currentRes, BorderLayout.CENTER);
		DisplayMode currentMode;
		Vector<DisplayMode> v = new Vector<DisplayMode>();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = graphicsEnvironment.getScreenDevices();
		for (int cnt = 0; cnt < devices.length; cnt++) {

			DisplayMode m = devices[cnt].getDisplayMode();
			if (devices[cnt].equals(graphicsEnvironment
					.getDefaultScreenDevice())) {
				currentRes.setText("Current resolution: " + m.getWidth() + "x"
						+ m.getHeight() + " (" + m.getRefreshRate() + "Hz)");

				for (int i = 0; i < devices[cnt].getDisplayModes().length; i++) {
					v.add(devices[cnt].getDisplayModes()[i]);
				}
			}
		}

		final JComboBox<DisplayMode> comboBox = new JComboBox<DisplayMode>(v);
		comboBox.setRenderer(new ListCellRenderer<DisplayMode>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends DisplayMode> list, DisplayMode value,
					int index, boolean isSelected, boolean cellHasFocus) {
				return new JLabel(value.getWidth() + "x" + value.getHeight()
						+ " (" + value.getRefreshRate() + "Hz)");
			}
		});
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fullscreenRadio.isSelected()) {
					startFullScreen((DisplayMode) comboBox.getSelectedItem());
				} else {
					startWindowed();
				}
				cf.dispose();
			}
		});
		okButton.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					okButton.doClick();
			}
		});
		currentMode = graphicsEnvironment.getDefaultScreenDevice()
				.getDisplayMode();
		comboBox.setSelectedItem(currentMode);
		fullPanel.add(comboBox, BorderLayout.SOUTH);
		panel.add(okButton, BorderLayout.SOUTH);
		cf.setContentPane(panel);
		cf.pack();
		cf.setSize(new Dimension(300, 200));
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cf.setLocationRelativeTo(null);
		cf.setVisible(true);
		okButton.requestFocus();
	}
}