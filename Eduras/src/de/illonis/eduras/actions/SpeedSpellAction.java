package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

public class SpeedSpellAction extends RTSAction {

	private final static Logger L = EduLog.getLoggerFor(SpeedSpellAction.class
			.getName());

	private Unit unitToSpeedUp;

	public SpeedSpellAction(Player executingPlayer, Unit unitToSpeedUp) {
		super(executingPlayer, S.Server.spell_speed_costs);

		this.unitToSpeedUp = unitToSpeedUp;
	}

	@Override
	protected void executeAction(GameInformation info) {
		info.getEventTriggerer().speedUpObjectForSomeTime(unitToSpeedUp,
				S.Server.spell_speed_duration, S.Server.spell_speed_amount);
	}
}
