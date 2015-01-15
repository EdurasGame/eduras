package de.illonis.eduras.gameclient.gui.hud;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.units.InteractMode;

public class SpectatorResourceDisplay extends ResourceDisplay {

	private final static Logger L = EduLog
			.getLoggerFor(SpectatorResourceDisplay.class.getName());

	private int index;
	private Team team;
	private float income;
	private float incomeY;
	private Font smallFont;

	protected SpectatorResourceDisplay(UserInterface gui, int index) {
		super(gui);
		this.index = index;
		team = null;
		visibleForSpectator = true;
		income = 0;
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		return GameMode.GameModeNumber.EDURA == gameMode.getNumber();
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		smallFont.drawString(screenX, incomeY, String.format("+ %.1f", income),
				Color.white);
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		boolean ok = super.init(g, windowWidth, windowHeight);
		screenY = 10;
		screenX = windowWidth / 4 + (index * (windowWidth / 2));
		smallFont = FontCache.getFont(FontKey.SMALL_FONT, g);
		incomeY = screenY + icon.getHeight();
		return ok;
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

	private void recalculateIncome() {
		Collection<GameObject> allBasesAsObjects = getInfo().findObjectsByType(
				ObjectType.NEUTRAL_BASE);
		Collection<Base> allBases = new LinkedList<Base>();
		for (GameObject go : allBasesAsObjects) {
			allBases.add((Base) go);
		}

		income = 0;
		for (GameObject object : allBasesAsObjects) {
			if (!(object instanceof Base)) {
				L.severe("Object doesn't have the expected type");
				continue;
			}

			Base base = (Base) object;
			if (team != null && team.equals(base.getCurrentOwnerTeam())) {
				income += (base
						.getResourceGenerateAmountPerTimeInterval(allBases) * ((float) 1000 / base
						.getResourceGenerateTimeInterval()));
			}
		}
	}

	@Override
	public void onBaseConquered(Base base, Team conqueringTeam) {
		recalculateIncome();
	}

	@Override
	public void onStartRound() {
		recalculateIncome();
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		if (team != null
				&& team.getTeamId() == setTeamResourceEvent.getTeamId()) {
			resAmount = setTeamResourceEvent.getNewAmount();
		}
	}
}
