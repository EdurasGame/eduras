package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;

public class ScoutSpellButton extends ActionButton {

	public ScoutSpellButton(GamePanelReactor reactor) {
		super("Vision spell", ImageKey.ACTION_SPELL_SCOUT, reactor);
	}

	@Override
	protected void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_POSITION_FOR_SCOUT);
	}
}
