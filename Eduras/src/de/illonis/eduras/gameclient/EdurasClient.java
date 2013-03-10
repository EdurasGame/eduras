package de.illonis.eduras.gameclient;

import java.awt.GraphicsEnvironment;
import java.util.logging.Level;

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

		GameClient client = new GameClient();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		ClientFrame f = new FullScreenClientFrame(
				graphicsEnvironment.getDefaultScreenDevice(),
				graphicsEnvironment.getDefaultScreenDevice().getDisplayMode(),
				client);
		client.useFrame(f);
		client.startGui();
		// EduLog.setTrackDetail(3);
	}
}