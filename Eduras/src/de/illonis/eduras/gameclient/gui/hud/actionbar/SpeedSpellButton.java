package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.settings.S;

public class SpeedSpellButton extends UnitSpellButton {

	private final static Logger L = EduLog.getLoggerFor(SpeedSpellButton.class
			.getName());

	public SpeedSpellButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_SPELL_SPEED, reactor, GameEventNumber.SPEED_SPELL);
		label = Localization.getString("Client.gui.actions.spell_speed_title");
		description = Localization
				.getString("Client.gui.actions.spell_speed_text");
	}

	@Override
	public int getCosts() {
		return S.Server.spell_speed_costs;
	}
}
