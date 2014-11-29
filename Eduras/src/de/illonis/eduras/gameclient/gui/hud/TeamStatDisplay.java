package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Statistic;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.units.InteractMode;

/**
 * Displays each teams total kills in spectator mode.
 * 
 * @author illonis
 * 
 */
public class TeamStatDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(TeamStatDisplay.class
			.getName());
	private int windowWidth = 0;

	protected TeamStatDisplay(UserInterface gui) {
		super(gui);
		visibleForSpectator = true;
		// workaround for elements only visible in spectator mode.
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
	}

	@Override
	public void render(Graphics g) {
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		Font bigFont = FontCache.getFont(FontKey.HUGE_FONT, g);
		Statistic stats = getInfo().getStatistics();
		List<Team> teams = new LinkedList<Team>(getInfo().getTeams());
		if (teams.size() != 2)
			return;
		String left = stats.getKillsByTeam(teams.get(0)) + "";
		String right = stats.getKillsByTeam(teams.get(1)) + "";
		String middle = ":";
		float y = screenY + 2 * font.getLineHeight();
		screenX = (windowWidth - bigFont.getWidth(middle)) / 2
				- bigFont.getWidth(left);
		bigFont.drawString(screenX, y, left + middle + right, Color.white);
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		return gameMode.getNumber() == GameModeNumber.EDURA
				|| gameMode.getNumber() == GameModeNumber.TEAM_DEATHMATCH;
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = 0;
		windowWidth = newWidth;
	}
}
