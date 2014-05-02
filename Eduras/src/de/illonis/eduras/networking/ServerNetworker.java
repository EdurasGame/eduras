package de.illonis.eduras.networking;

import java.util.logging.Logger;

import de.eduras.eventingserver.ServerNetworkEventHandler;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;

/**
 * Handles connection events of the Eduras Server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerNetworker implements ServerNetworkEventHandler {

	private final static Logger L = EduLog.getLoggerFor("ServerNetworker");

	private final GameInformation gameInfo;

	/**
	 * Create a new ServerNetworker.
	 * 
	 * @param gameInfo
	 *            The gameInfo of the current game.
	 */
	public ServerNetworker(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
	}

	@Override
	public void onClientDisconnected(int clientId) {
		L.info("User with id #" + clientId
				+ " disconnected from Eduras Server.");
		gameInfo.getEventTriggerer().notifyPlayerLeft(clientId);
		gameInfo.getGameSettings().getGameMode().onDisconnect(clientId);
	}

	@Override
	public void onClientConnected(int clientId) {
		L.info("User with id #" + clientId + " connected to Eduras Server.");
	}

}
