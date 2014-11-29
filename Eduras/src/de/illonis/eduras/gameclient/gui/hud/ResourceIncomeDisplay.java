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
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.units.InteractMode;

public class ResourceIncomeDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog
			.getLoggerFor(ResourceIncomeDisplay.class.getName());

	private int index;
	private Team team;
	private float income;

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
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = 30;
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
	public void onBaseConquered(Base base, Team conqueringTeam) {
		recalculateIncome();
	}

	@Override
	public void onStartRound() {
		recalculateIncome();
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
	public void render(Graphics g) {
		Font font = FontCache.getFont(FontKey.SMALL_FONT, g);

		font.drawString(screenX, screenY, String.format("%.1f", income),
				Color.white);
	}
}
