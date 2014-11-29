package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.units.InteractMode;

public class SpectatorResourceDisplay extends ResourceDisplay {

	private final static Logger L = EduLog
			.getLoggerFor(SpectatorResourceDisplay.class.getName());

	private int index;
	private Team team;

	protected SpectatorResourceDisplay(UserInterface gui, int index) {
		super(gui);
		this.index = index;
		team = null;
		visibleForSpectator = true;
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		return GameMode.GameModeNumber.EDURA == gameMode.getNumber();
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = 10;
		screenX = newWidth / 4 + (index * (newWidth / 2));
	}

	@Override
	public void onTeamsSet(LinkedList<Team> teamList) {
		LinkedList<Team> teams = new LinkedList<Team>(getInfo().getTeams());
		if (teams.size() > index) {
			team = teams.get(index);
		} else {
			team = null;
		}
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		if (team != null
				&& team.getTeamId() == setTeamResourceEvent.getTeamId()) {
			resAmount = setTeamResourceEvent.getNewAmount();
		}
	}
}
