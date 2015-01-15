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
 * Displays each teams total score in spectator mode.
 * 
 * @author illonis
 * 
 */
public class TeamScoreDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(TeamScoreDisplay.class
			.getName());
	private final String middle = " : ";
	private float centerX, y;
	private Font font;

	protected TeamScoreDisplay(UserInterface gui) {
		super(gui);
		visibleForSpectator = true;
		// workaround for elements only visible in spectator mode.
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
	}

	@Override
	public void render(Graphics g) {

		Statistic stats = getInfo().getStatistics();
		List<Team> teams = new LinkedList<Team>(getInfo().getTeams());
		if (teams.size() != 2)
			return;
		String left;
		String right;

		left = teams.get(0).getName() + " "
				+ stats.getScoreOfTeam(teams.get(0));
		right = stats.getScoreOfTeam(teams.get(1)) + " "
				+ teams.get(1).getName();
		screenX = centerX - font.getWidth(left);
		font.drawString(screenX, y, left, teams.get(0).getColor());
		font.drawString(screenX + font.getWidth(left), y, middle, Color.white);
		font.drawString(screenX + font.getWidth(left + middle), y, right, teams
				.get(1).getColor());
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		return gameMode.getNumber() == GameModeNumber.EDURA;
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		screenY = 0;
		centerX = (windowWidth - font.getWidth(middle)) / 2;
		y = screenY + font.getLineHeight();
		return true;
	}
}
