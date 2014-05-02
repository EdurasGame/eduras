package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;

/**
 * Heals a unit that is clicked after clicking this button.
 * 
 * @author illonis
 * 
 */
public class HealButton extends ActionButton {

	private final static String BASE_STRING = "Heal a unit";

	/**
	 * Creates a new healbutton.
	 * 
	 * @param reactor
	 *            the reactor.
	 */
	public HealButton(GamePanelReactor reactor) {
		super(BASE_STRING, ImageKey.ACTION_HEAL, reactor);
	}

	@Override
	public void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_TARGET_FOR_HEAL);
	}
}
