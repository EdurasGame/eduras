package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.settings.S;

/**
 * Resurrects a player.
 * 
 * @author illonis
 * 
 */
public class RezzButton extends ActionButton {

	/**
	 * Creates a rezz-button for given player.
	 * 
	 * @param reactor
	 *            the reactor.
	 */
	public RezzButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_RESURRECT_PLAYER, reactor);

		label = Localization.getString("Client.gui.actions.spell_rezz_title");
		description = Localization
				.getString("Client.gui.actions.spell_rezz_text");
	}

	@Override
	public void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_PLAYER_FOR_REZZ);
	}

	@Override
	public int getCosts() {
		return S.Server.gm_edura_action_respawnplayer_cost;
	}
}
