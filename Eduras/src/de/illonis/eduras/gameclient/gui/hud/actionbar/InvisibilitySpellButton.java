package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.settings.S;

public class InvisibilitySpellButton extends UnitSpellButton {

	private final static Logger L = EduLog
			.getLoggerFor(InvisibilitySpellButton.class.getName());

	public InvisibilitySpellButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_SPELL_INVISIBILITY, reactor,
				GameEventNumber.INVISIBILITY_SPELL);
	}

	@Override
	public String getLabel() {
		return "Make invisible [Costs: " + getCosts() + "]";
	}

	@Override
	public int getCosts() {
		return S.Server.spell_invisibility_costs;
	}
}
