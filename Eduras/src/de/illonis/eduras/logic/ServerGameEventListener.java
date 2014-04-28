package de.illonis.eduras.logic;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.ServerInterface;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.gameclient.GameEventAdapter;

/**
 * Defines how the server reacts on certain events in the logic that are the
 * same on client and server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerGameEventListener extends GameEventAdapter {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private ServerInterface server;

	/**
	 * Create a new ServerGameEventListener.
	 * 
	 * @param server
	 */
	public ServerGameEventListener(ServerInterface server) {
		this.server = server;
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		try {
			server.sendEventToAll(event);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.log(Level.SEVERE, "Error appeared while trying to send event.", e);
		}
	}

}
