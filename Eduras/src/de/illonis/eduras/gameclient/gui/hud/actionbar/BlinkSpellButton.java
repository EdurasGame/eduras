package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.settings.S;

public class BlinkSpellButton extends UnitSpellButton {

	private final static Logger L = EduLog.getLoggerFor(BlinkSpellButton.class
			.getName());

	public BlinkSpellButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_SPELL_BLINK, reactor, GameEventNumber.BLINK_SPELL);
		label = Localization
				.getString("Client.gui.actions.spell_giveblink_title");
		description = Localization
				.getString("Client.gui.actions.spell_giveblink_text");
	}

	@Override
	public int getCosts() {
		return S.Server.spell_blink_costs;
	}
}
