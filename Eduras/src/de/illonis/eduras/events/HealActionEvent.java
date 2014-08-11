package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Notifies the server that a player wants to heal a unit.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class HealActionEvent extends UnitSpellActionEvent {

	private final static Logger L = EduLog.getLoggerFor(HealActionEvent.class
			.getName());

	/**
	 * Create a new HealActionEvent.
	 * 
	 * @param executingPlayer
	 *            The player who wants to execute the heal.
	 * @param idOfUnitToHeal
	 *            id of the unit to heal.
	 */
	public HealActionEvent(int executingPlayer, int idOfUnitToHeal) {
		super(GameEventNumber.HEAL_ACTION, executingPlayer, idOfUnitToHeal);

		putArgument(idOfUnitToHeal);
	}
}
