package de.illonis.eduras.gameclient;

import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logger.EduLog.LogMode;

public class EdurasClient {

	public static void main(String[] args) {
		// new LoggerGui().setVisible(true);

		EduLog.setLogOutput(LogMode.NONE);
		GameClient client = new GameClient();
		client.startGui();
		// EduLog.setTrackDetail(3);
	}

}
