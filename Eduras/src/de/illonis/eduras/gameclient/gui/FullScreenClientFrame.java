package de.illonis.eduras.gameclient.gui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import de.illonis.eduras.gameclient.GameClient;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;

/**
 * A full screen client frame.
 * 
 * @author illonis
 * 
 */
public class FullScreenClientFrame extends ClientFrame {

	private static final long serialVersionUID = 1L;
	private DisplayMode oldDisplayMode;
	private GraphicsDevice device;

	/**
	 * Creates a new fullscreen frame that displays on given environment.
	 * 
	 * @param graphicsDevice
	 *            target graphics device.
	 * @param mode
	 *            display mode.
	 * @param client
	 *            gameclient.
	 */
	public FullScreenClientFrame(GraphicsDevice graphicsDevice,
			DisplayMode mode, GameClient client) {
		super(client);
		this.oldDisplayMode = graphicsDevice.getDisplayMode();
		this.device = graphicsDevice;
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
			EduLog.error(Localization.getString("Client.errors.nofullscreen"));
		}
	}

	@Override
	public void onDisconnect(int clientId) {
		if (clientId == client.getOwnerID())
			device.setDisplayMode(oldDisplayMode);
		super.onDisconnect(clientId);
	}
}
