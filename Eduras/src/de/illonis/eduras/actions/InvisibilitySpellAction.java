package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

public class InvisibilitySpellAction extends RTSAction {

	private final static Logger L = EduLog
			.getLoggerFor(InvisibilitySpellAction.class.getName());

	private Unit unitToMakeInvisible;

	public InvisibilitySpellAction(Player executingPlayer,
			Unit unitToMakeInvisible) {
		super(executingPlayer, S.Server.spell_invisibility_costs);
		this.unitToMakeInvisible = unitToMakeInvisible;
	}

	@Override
	protected void executeAction(GameInformation info) {
		info.getEventTriggerer().makeInvisibleForSomeTime(unitToMakeInvisible,
				S.Server.spell_invisibility_duration);
	}
}
