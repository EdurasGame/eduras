package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.actions.ScoutSpellAction;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.settings.S;

/**
 * The button to trigger the {@link ScoutSpellAction}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ScoutSpellButton extends ActionButton {

	/**
	 * Create the button.
	 * 
	 * @param reactor
	 *            element to react on click.
	 */
	public ScoutSpellButton(GamePanelReactor reactor) {
		super("Vision spell [Costs: " + S.Server.spell_scout_costs + "]",
				ImageKey.ACTION_SPELL_SCOUT, reactor);
	}

	@Override
	protected void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_POSITION_FOR_SCOUT);
	}
}
