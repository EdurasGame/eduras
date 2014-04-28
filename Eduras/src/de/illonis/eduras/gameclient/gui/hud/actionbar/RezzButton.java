package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Resurrects a player.
 * 
 * @author illonis
 * 
 */
public class RezzButton extends ActionButton {
	private final static String BASE_STRING = "Resurrect %s";
	private final PlayerMainFigure target;

	/**
	 * Creates a rezz-button for given player.
	 * 
	 * @param player
	 *            the player that is resurrected when this button is used.
	 * @param reactor
	 *            the reactor.
	 */
	public RezzButton(PlayerMainFigure player, GamePanelReactor reactor) {
		super(String.format(BASE_STRING, player.getName()),
				ImageKey.ACTION_HEAL, reactor);
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
		if (event.getKilled() == target.getId())
			setEnabled(true);
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		if (event.getOwner() == target.getOwner())
			setEnabled(false);
	}
}
