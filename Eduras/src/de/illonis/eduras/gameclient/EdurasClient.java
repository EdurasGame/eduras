package de.illonis.eduras.gameclient;

import java.util.logging.Level;

import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logger.EduLog.LogMode;

/**
 * Eduras? Game client for end user.
 * 
 * @author illonis
 * 
 */
public class EdurasClient {

	public static void main(String[] args) {
		// new LoggerGui().setVisible(true);
		EduLog.setLogOutput(LogMode.CONSOLE);
		EduLog.setLogLimit(Level.SEVERE);

		GameClient client = new GameClient();
		client.startGui();
		// EduLog.setTrackDetail(3);
	}
}