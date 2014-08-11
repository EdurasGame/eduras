package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class UnitSpellActionEvent extends RTSActionEvent {
	private final static Logger L = EduLog
			.getLoggerFor(UnitSpellActionEvent.class.getName());

	private int idOfUnitToCastSpellOn;

	/**
	 * Create a new {@link UnitSpellActionEvent}.
	 * 
	 * @param eventNumber
	 * @param executingPlayer
	 *            The player who wants to execute the heal.
	 * @param idOfUnitToCastSpellOn
	 *            id of the unit to cast the spell on.
	 */
	public UnitSpellActionEvent(GameEventNumber eventNumber,
			int executingPlayer, int idOfUnitToCastSpellOn) {
		super(eventNumber, executingPlayer);

		putArgument(idOfUnitToCastSpellOn);
		this.idOfUnitToCastSpellOn = idOfUnitToCastSpellOn;
	}

	public int getIdOfUnitToCastSpellOn() {
		return idOfUnitToCastSpellOn;
	}

}
