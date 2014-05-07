package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Notifies the server that a player wants to heal a unit.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class HealActionEvent extends RTSActionEvent {

	private final static Logger L = EduLog.getLoggerFor(HealActionEvent.class
			.getName());

	private int idOfUnitToHeal;

	/**
	 * Create a new HealActionEvent.
	 * 
	 * @param executingPlayer
	 *            The player who wants to execute the heal.
	 * @param idOfUnitToHeal
	 *            id of the unit to heal.
	 */
	public HealActionEvent(int executingPlayer, int idOfUnitToHeal) {
		super(GameEventNumber.HEAL_ACTION, executingPlayer);

		putArgument(idOfUnitToHeal);
		this.idOfUnitToHeal = idOfUnitToHeal;
	}

	/**
	 * Returns the id of the unit to heal
	 * 
	 * @return id
	 */
	public int getIdOfUnitToHeal() {
		return idOfUnitToHeal;
	}
}
