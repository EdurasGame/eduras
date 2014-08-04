package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.Player;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.settings.S;

/**
 * Resurrects a player.
 * 
 * @author illonis
 * 
 */
public class RezzButton extends ActionButton {
	private final Player target;

	/**
	 * Creates a rezz-button for given player.
	 * 
	 * @param player
	 *            the player that is resurrected when this button is used.
	 * @param reactor
	 *            the reactor.
	 */
	public RezzButton(Player player, GamePanelReactor reactor) {
		super(ImageKey.ACTION_RESURRECT_PLAYER, reactor);
		this.target = player;
		if (target.getPlayerMainFigure() != null
				&& !target.getPlayerMainFigure().isDead()) {
			setEnabled(false);
		}
	}

	@Override
	public void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_BASE_FOR_REZZ);
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentResurrectTarget(target);
	}

	/**
	 * Returns the player that will be rezzed when this button is used.
	 * 
	 * @return player to be rezzed by clicking this button
	 */
	public Player getTarget() {
		return target;
	}

	@Override
	public String getLabel() {
		String label = "Resurrect %s [Costs: %d]";
		return String.format(label, target.getName(), getCosts());
	}

	@Override
	public int getCosts() {
		return S.Server.gm_edura_action_respawnplayer_cost;
	}
}
