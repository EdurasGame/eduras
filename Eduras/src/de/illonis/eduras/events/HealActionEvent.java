package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class HealActionEvent extends RTSActionEvent {

	private final static Logger L = EduLog.getLoggerFor(HealActionEvent.class
			.getName());

	private int idOfUnitToHeal;

	public HealActionEvent(int executingPlayer, int idOfUnitToHeal) {
		super(GameEventNumber.HEAL_ACTION, executingPlayer);

		putArgument(idOfUnitToHeal);
		this.idOfUnitToHeal = idOfUnitToHeal;
	}

	public int getIdOfUnitToHeal() {
		return idOfUnitToHeal;
	}
}
