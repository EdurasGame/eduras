package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * An {@link RTSAction} that heals a unit.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class HealSpellAction extends RTSAction {

	private final static Logger L = EduLog.getLoggerFor(HealSpellAction.class
			.getName());

	private Unit unitToHeal;

	/**
	 * Create a new HealSpellAction.
	 * 
	 * @param executingPlayer
	 * @param unitToHeal
	 */
	public HealSpellAction(Player executingPlayer, Unit unitToHeal) {
		super(executingPlayer, S.Server.spell_heal_costs);

		this.unitToHeal = unitToHeal;
	}

	@Override
	protected void executeAction(GameInformation info) {
		info.getEventTriggerer().changeHealthByAmount(unitToHeal,
				S.Server.spell_heal_amount);
	}
}
