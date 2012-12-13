package de.illonis.eduras.gameclient;

/**
 * Eduras? Game client for end user.
 * 
 * @author illonis
 * 
 */
public class EdurasClient {

	public static void main(String[] args) {
		// new LoggerGui().setVisible(true);

		GameClient client = new GameClient();
		client.startGui();
		// EduLog.setTrackDetail(3);
	}
}