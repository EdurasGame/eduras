package de.illonis.eduras;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * A spectator is a client who receives all the game information that any player
 * receives. However, the server doesn't take the spectator into account for any
 * other purpose, neither are the players aware that there is a spectator (for
 * now).
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 *
 */
public class Spectator {
	private final static Logger L = EduLog.getLoggerFor(Spectator.class
			.getName());

	private final String name;
	private final int id;

	/**
	 * Create a new spectator.
	 * 
	 * @param clientId
	 * @param name
	 *            The spectator's name
	 */
	public Spectator(int clientId, String name) {
		this.name = name;
		this.id = clientId;
	}

	/**
	 * Returns the spectator's name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the spectator's id.
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

}
