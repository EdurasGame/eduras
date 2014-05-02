package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBar;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.gameobjects.GameObject.Relation;
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
public class ResurrectPage extends ActionBarPage {
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
		super(PageNumber.RESURRECT);
		this.reactor = reactor;
		this.bar = bar;
	}

	@Override
	public void onGameReady() {
		// TODO: add all existing players.
	}

	@Override
	public void onDeath(DeathEvent event) {
		// we need to manually notify buttons because we cannot add them to the
		// hudnotifier.
		for (ActionButton button : getButtons()) {
			button.onDeath(event);
		}
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		for (ActionButton button : getButtons()) {
			button.onRespawn(event);
		}
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		// TODO: check for duplicate entries (just to be sure)
		InformationProvider infoPro = EdurasInitializer.getInstance()
				.getInformationProvider();

		try {
			Player joinedPlayer = infoPro.getPlayerByOwnerId(ownerId);
			Player player = infoPro.getPlayer();
			if (joinedPlayer.getTeam().equals(player.getTeam())) {
				RezzButton rezzButton = new RezzButton(joinedPlayer, reactor);
				rezzButton.cacheReady();
				addButton(rezzButton);
				if (bar.getCurrentPage() == getId())
					bar.setPage(getId());
			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Joined player cannot be found.", e);
		}
	}

	@Override
	public void onPlayerLeft(int ownerId) {
		// TODO: remove button from bar.
	}
}
