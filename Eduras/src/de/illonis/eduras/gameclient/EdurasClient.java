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
import java.io.IOException;
import java.util.Vector;
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
import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.FullScreenClientFrame;
import de.illonis.eduras.networking.Client;

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
	 * Starts Eduras? client.
	 * 
	 * @param args
	 *            First argument is considered to be the port to which the
	 *            client is bound.
	 */
	public static void main(String[] args) {
		// new LoggerGui().setVisible(true);
		try {
			EduLog.init("client.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// EduLog.setConsoleLogLimit(Level.OFF);

		if (args.length > 0) {
			try {
				Client.PORT = Integer.parseInt(args[0]);
				if (Client.PORT < 1024 || Client.PORT > 49151) {
					throw new Exception();
				}
			} catch (Exception e) {
				L.severe("Given port is not a valid value!");
				return;
			}
		}

		// Note that this is very bad coded due to testing ;)
		buildChooserFrame();

		// EduLog.setTrackDetail(3);
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