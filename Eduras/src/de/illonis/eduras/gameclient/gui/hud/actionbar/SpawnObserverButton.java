package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.settings.S;

/**
 * A button that spawns an observer unit.
 * 
 * @author illonis
 * 
 */
public class SpawnObserverButton extends ActionButton {

	/**
	 * @param reactor
	 *            the reactor.
	 */
	public SpawnObserverButton(GamePanelReactor reactor) {
		super(ImageKey.ACTION_SPAWN_OBSERVER, reactor);
		label = Localization
				.getString("Client.gui.actions.spell_spawnobserver_title");
		description = Localization
				.getString("Client.gui.actions.spell_spawnobserver_text");
	}

	@Override
	protected void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_POSITION_FOR_OBSERVERUNIT);
	}

	@Override
	public int getCosts() {
		return S.Server.unit_observer_costs;
	}
}
