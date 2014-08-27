package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

public class BlinkSpellAction extends RTSAction {

	private final static Logger L = EduLog.getLoggerFor(BlinkSpellAction.class
			.getName());

	private PlayerMainFigure playerToGiveBlinkCharge;

	public BlinkSpellAction(Player executingPlayer,
			PlayerMainFigure playerToGiveBlinkCharge) {
		super(executingPlayer, S.Server.spell_blink_costs);
		this.playerToGiveBlinkCharge = playerToGiveBlinkCharge;
	}

	@Override
	protected void executeAction(GameInformation info) {
		info.getEventTriggerer().changeBlinkChargesBy(
				playerToGiveBlinkCharge.getPlayer(), 1);
	}
}
