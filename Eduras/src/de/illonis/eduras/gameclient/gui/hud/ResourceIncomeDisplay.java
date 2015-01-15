package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Team;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.units.InteractMode;

@Deprecated
public class ResourceIncomeDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog
			.getLoggerFor(ResourceIncomeDisplay.class.getName());

	private int index;
	private Team team;

	protected ResourceIncomeDisplay(UserInterface gui, int index) {
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
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		screenY = 40;
		screenX = windowWidth / 4 + (index * (windowWidth / 2));
		return true;
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
	public void render(Graphics g) {

	}
}
