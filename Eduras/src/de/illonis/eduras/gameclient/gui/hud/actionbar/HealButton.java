package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.settings.S;

/**
 * Heals a unit that is clicked after clicking this button.
 * 
 * @author illonis
 * 
 */
public class HealButton extends UnitSpellButton {

	/**
	 * Creates a new healbutton.
	 * 
	 * @param reactor
	 *            the reactor.
	 */
	public HealButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_HEAL, reactor, GameEventNumber.HEAL_ACTION);
	}

	@Override
	public String getLabel() {
		final String BASE_STRING = "Heal a unit [Costs: " + getCosts() + "]";
		return BASE_STRING;
	}

	@Override
	public int getCosts() {
		return S.Server.spell_heal_costs;
	}
}
