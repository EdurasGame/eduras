package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.units.InteractMode;

public class PlayerResourceDisplay extends ResourceDisplay {

	private final static Logger L = EduLog
			.getLoggerFor(PlayerResourceDisplay.class.getName());

	private Player player;

	protected PlayerResourceDisplay(UserInterface gui) {
		super(gui);

		setActiveInteractModes(InteractMode.MODE_STRATEGY);
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		if (player == null) {
			return;
		}

		try {
			resAmount = player.getTeam().getResource();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.WARNING,
					"Player doesn't have a team (yet). Won't draw the resources",
					e);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth * 3 / 4;
		screenY = 10;
	}

	@Override
	public void onGameReady() {
		try {
			player = getInfo().getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE,
					"Player not found after playerinformation received.", e);
		}
	}
}
