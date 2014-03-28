package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * This event denotes that a base was conquered.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class AreaConqueredEvent extends GameEvent {
	private final static Logger L = EduLog
			.getLoggerFor(AreaConqueredEvent.class.getName());

	private final int conqueringTeam;
	private final int baseId;

	/**
	 * Create a new BaseConqueredEvent.
	 * 
	 * @param idOfBase
	 *            The id of the base conquered.
	 * @param teamId
	 *            The id of the team that conquered the base.
	 */
	public AreaConqueredEvent(int idOfBase, int teamId) {
		super(GameEventNumber.BASE_CONQUERED);

		conqueringTeam = teamId;
		baseId = idOfBase;

		putArgument(idOfBase);
		putArgument(teamId);
	}

	/**
	 * Get the id of the team that conquered the base.
	 * 
	 * @return id of team
	 */
	public int getConqueringTeam() {
		return conqueringTeam;
	}

	/**
	 * Get the object id of the base conquered.
	 * 
	 * @return object id of base.
	 */
	public int getBaseId() {
		return baseId;
	}

}
