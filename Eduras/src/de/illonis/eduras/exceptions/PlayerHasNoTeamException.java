package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;

/**
 * Thrown if a player doesn't have a team.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PlayerHasNoTeamException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger L = EduLog
			.getLoggerFor(PlayerHasNoTeamException.class.getName());

	/**
	 * Creates this Exception.
	 * 
	 * @param player
	 *            the player who doesn't have a team
	 */
	public PlayerHasNoTeamException(Player player) {
		super("Player " + player.getName() + " doesn't have a team!");
	}
}
