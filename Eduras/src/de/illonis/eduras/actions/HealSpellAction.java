package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

public class HealSpellAction extends RTSAction {

	private final static Logger L = EduLog.getLoggerFor(HealSpellAction.class
			.getName());

	private Unit unitToHeal;

	public HealSpellAction(PlayerMainFigure executingPlayer, Unit unitToHeal) {
		super(executingPlayer, S.spell_heal_costs);

		this.unitToHeal = unitToHeal;
	}

	@Override
	protected void executeAction(GameInformation info) {
		info.getEventTriggerer().changeHealthByAmount(unitToHeal,
				S.spell_heal_amount);
	}
}
