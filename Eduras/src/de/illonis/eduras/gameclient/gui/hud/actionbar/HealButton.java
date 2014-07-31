package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.settings.S;

/**
 * Heals a unit that is clicked after clicking this button.
 * 
 * @author illonis
 * 
 */
public class HealButton extends ActionButton {

	/**
	 * Creates a new healbutton.
	 * 
	 * @param reactor
	 *            the reactor.
	 */
	public HealButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_HEAL, reactor);
	}

	@Override
	public void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_TARGET_FOR_HEAL);
	}

	@Override
	public String getLabel() {
		final String BASE_STRING = "Heal a unit [Costs: "
				+ S.Server.spell_heal_costs + "]";
		return BASE_STRING;
	}
}
