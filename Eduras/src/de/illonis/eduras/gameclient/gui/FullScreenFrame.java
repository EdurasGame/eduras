package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FullScreenFrame extends JFrame implements ActionListener {

	private GraphicsDevice graphicsDevice;
	private DisplayMode origDisplayMode;
	private JButton exitButton = new JButton("Exit Full-Screen Mode");
	private JLabel centerLabel;

	private static final long serialVersionUID = 1L;

	public FullScreenFrame(GraphicsDevice graphicsDevice, DisplayMode mode) {
		exitButton.addActionListener(this);
		origDisplayMode = graphicsDevice.getDisplayMode();
		this.graphicsDevice = graphicsDevice;

		buildGui();
		if (graphicsDevice.isFullScreenSupported()) {
			// Enter full-screen mode with an undecorated,
			// non-resizable JFrame object.
			setUndecorated(true);
			setResizable(false);
			// Make it happen!
			graphicsDevice.setFullScreenWindow(this);

			graphicsDevice.setDisplayMode(mode);

			validate();
		} else {
			System.out.println("Full-screen mode not supported");
		}// end else

	}

	private void buildGui() {
		getContentPane().add(exitButton, BorderLayout.NORTH);

		// Place four labels in the JFrame solely for the
		// purpose of showing that it is a full-screen
		// undecorated JFrame.
		JLabel eastLabel = new JLabel("     East     ");
		eastLabel.setOpaque(true);
		eastLabel.setBackground(Color.RED);
		getContentPane().add(eastLabel, BorderLayout.EAST);

		JLabel southLabel = new JLabel("South", SwingConstants.CENTER);
		southLabel.setOpaque(true);
		southLabel.setBackground(Color.GREEN);
		getContentPane().add(southLabel, BorderLayout.SOUTH);

		JLabel westLabel = new JLabel("     West     ");
		westLabel.setOpaque(true);
		westLabel.setBackground(Color.RED);
		getContentPane().add(westLabel, BorderLayout.WEST);

		centerLabel = new JLabel("Center", SwingConstants.CENTER);
		centerLabel.setOpaque(true);
		centerLabel.setBackground(Color.WHITE);
		getContentPane().add(centerLabel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Restore the original display mode
		graphicsDevice.setDisplayMode(origDisplayMode);

		dispose();

	}

	public void draw() {

		centerLabel.getGraphics().setColor(Color.GREEN);
		centerLabel.getGraphics().fillRect(100, 100, 50, 50);

	}
}
