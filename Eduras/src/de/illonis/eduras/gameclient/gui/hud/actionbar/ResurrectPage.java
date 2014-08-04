package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBar;
import de.illonis.eduras.gameclient.gui.hud.ActionBarSubPage;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Displays resurrection buttons.<br>
 * This page handles rezz-buttons display so that for each allied user a button
 * is displayed.
 * 
 * @author illonis
 * 
 */
public class ResurrectPage extends ActionBarSubPage {
	private final static Logger L = EduLog.getLoggerFor(ResurrectPage.class
			.getName());

	private final GamePanelReactor reactor;
	private final ActionBar bar;

	/**
	 * Creates a page for resurrection.
	 * 
	 * @param bar
	 *            the parent actionbar.
	 * @param reactor
	 *            the reactor.
	 */
	public ResurrectPage(ActionBar bar, GamePanelReactor reactor) {
		super(PageNumber.RESURRECT, PageNumber.MAIN, reactor, bar);
		this.reactor = reactor;
		this.bar = bar;
	}

	@Override
	public void onGameReady() {
		updateRezzButtons();
	}

	@Override
	public void onShown() {
		super.onShown();

		updateRezzButtons();
	}

	@Override
	public void onDeath(DeathEvent event) {
		updateRezzButtons();
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		updateRezzButtons();
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		updateRezzButtons();
	}

	private void addRezzButtonForPlayer(Player player)
			throws ObjectNotFoundException {
		InformationProvider infoPro = EdurasInitializer.getInstance()
				.getInformationProvider();

		if (isPlayerButtonAlreadyAvailable(player)) {
			return;
		}

		if (player.equals(infoPro.getPlayer())) {
			return;
		}

		Team teamOfPlayer;
		Team teamOfClientsPlayer;
		try {
			teamOfPlayer = player.getTeam();
			teamOfClientsPlayer = infoPro.getPlayer().getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE,
					"Both players should have a team at this point!", e);
			return;
		}

		if (teamOfPlayer.equals(teamOfClientsPlayer)) {
			RezzButton rezzButton = new RezzButton(player, reactor);
			addButton(rezzButton);
			// refresh bar if it is currently visible
			if (bar.getCurrentPage() == getId())
				bar.setPage(getId());
		}
	}

	private boolean isPlayerButtonAlreadyAvailable(Player player) {
		for (ActionButton button : getButtons()) {
			if (button instanceof RezzButton
					&& ((RezzButton) button).getTarget().equals(player)) {
				return true;
			}
		}
		return false;
	}

	private void updateRezzButtons() {
		// removeAllButtons();

		InformationProvider infoPro = EdurasInitializer.getInstance()
				.getInformationProvider();

		for (Player player : infoPro.getPlayers()) {
			try {
				addRezzButtonForPlayer(player);
			} catch (ObjectNotFoundException e) {
				L.log(Level.SEVERE, "Joined player cannot be found.", e);
				continue;
			}
		}
	}

	@Override
	public void onPlayerLeft(int ownerId) {
		updateRezzButtons();
	}
}
