package de.illonis.eduras.gameclient.gui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import de.illonis.eduras.gameclient.GameClient;

public class FullScreenClientFrame extends ClientFrame {

	private static final long serialVersionUID = 1L;
	private DisplayMode oldDisplayMode;
	private GraphicsDevice device;

	public FullScreenClientFrame(GraphicsDevice graphicsDevice,
			DisplayMode mode, GameClient client) {
		super(client);
		this.oldDisplayMode = graphicsDevice.getDisplayMode();
		this.device = graphicsDevice;
		setSize(1024, 768);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		if (graphicsDevice.isFullScreenSupported()) {
			// Enter full-screen mode with an undecorated,
			// non-resizable JFrame object.
			setUndecorated(true);
			setResizable(false);
			// Make it happen!
			graphicsDevice.setFullScreenWindow(this);

			graphicsDevice.setDisplayMode(mode);

			// validate();
		} else {
			System.out.println("Fullscreen is not supported.");
		}
	}

	@Override
	public void onDisconnect() {
		device.setDisplayMode(oldDisplayMode);
		super.onDisconnect();
	}
}
