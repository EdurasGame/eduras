package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.Player;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.RespawnEvent;
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
	private final static String BASE_STRING = "Resurrect %s [Costs: "
			+ " [Costs: " + S.Server.gm_edura_action_respawnplayer_cost + "]";
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
		super(String.format(BASE_STRING, player.getName()),
				ImageKey.ACTION_RESURRECT_PLAYER, reactor);
		this.target = player;
	}

	@Override
	public void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_BASE_FOR_REZZ);
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentResurrectTarget(target);
	}

	@Override
	public void onDeath(DeathEvent event) {
		if (event.getKilled() == target.getPlayerId())
			setEnabled(true);
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		if (event.getOwner() == target.getPlayerId())
			setEnabled(false);
	}

	/**
	 * Returns the player that will be rezzed when this button is used.
	 * 
	 * @return player to be rezzed by clicking this button
	 */
	public Player getTarget() {
		return target;
	}
}
