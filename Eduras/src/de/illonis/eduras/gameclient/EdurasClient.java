package de.illonis.eduras.gameclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;

import de.illonis.eduras.gameclient.gui.ClientFrame;
import de.illonis.eduras.gameclient.gui.FullScreenClientFrame;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logger.EduLog.LogMode;

/**
 * Eduras? Game client for end user.
 * 
 * @author illonis
 * 
 */
public class EdurasClient {

	/**
	 * Starts Eduras? client.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// new LoggerGui().setVisible(true);
		EduLog.setLogOutput(LogMode.CONSOLE);
		EduLog.setLogLimit(Level.SEVERE);

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

	protected static void startFullScreen() {
		GameClient client = new GameClient();

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		ClientFrame f = new FullScreenClientFrame(
				graphicsEnvironment.getDefaultScreenDevice(),
				graphicsEnvironment.getDefaultScreenDevice().getDisplayMode(),
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

		JButton okButton = new JButton("Start");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fullscreenRadio.isSelected()) {
					startFullScreen();
				} else {
					startWindowed();
				}
				cf.dispose();
			}
		});
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

		JComboBox<DisplayMode> comboBox = new JComboBox<DisplayMode>(v);
		comboBox.setRenderer(new ListCellRenderer<DisplayMode>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends DisplayMode> list, DisplayMode value,
					int index, boolean isSelected, boolean cellHasFocus) {
				return new JLabel(value.getWidth() + "x" + value.getHeight()
						+ " (" + value.getRefreshRate() + "Hz)");
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
	}
}