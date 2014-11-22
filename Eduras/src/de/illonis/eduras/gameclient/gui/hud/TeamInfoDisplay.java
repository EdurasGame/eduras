package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.units.InteractMode;

/**
 * Displays stats of a team.
 * 
 * @author illonis
 * 
 */
public class TeamInfoDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(TeamInfoDisplay.class
			.getName());

	private Team team;
	private final float width;

	private final LinkedList<TeamPlayerDisplay> playerBars;
	private final UserInterface gui;
	private final int index;
	private boolean scheduled;

	protected TeamInfoDisplay(UserInterface gui, int index) {
		super(gui);
		this.gui = gui;
		scheduled = true;
		this.index = index;
		team = null;
		visibleForSpectator = true;
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
		width = PlayerDisplay.PLAYER_BAR_WIDTH * GameRenderer.getRenderScale()
				+ 5;
		playerBars = new LinkedList<TeamPlayerDisplay>();
		zIndex = 0;
	}

	/**
	 * Sets the team to display.
	 * 
	 * @param team
	 *            new team.
	 */
	public void setTeam(Team team) {
		this.team = team;
		recalculate();
	}

	@Override
	public void render(Graphics g) {
		if (team == null)
			return;
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		float height = (2 * team.getPlayers().size() - 1)
				+ team.getPlayers().size() * font.getLineHeight() * 2 + 4;
		if (scheduled) {
			scheduled = false;
			for (TeamPlayerDisplay d : playerBars) {
				gui.removeGuiElement(d);
				d.getMouseHandler().removeClickableGuiElement(d);
			}
			playerBars.clear();
			int i = 0;
			for (Player p : team.getPlayers()) {
				TeamPlayerDisplay display = new TeamPlayerDisplay(gui, p);
				display.setLocation(screenX + 1,
						screenY + 2 + i * (font.getLineHeight() * 2 + 2));
				playerBars.add(display);
				i++;
			}
		}
		if (team.getPlayers().size() != 0) {
			g.setColor(team.getColor());
			g.fillRect(screenX, screenY, width, height);
		}
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		recalculate();
	}

	private void recalculate() {
		scheduled = true;
	}

	@Override
	public void onPlayerTeamChanged(int ownerId) {
		recalculate();
	}

	@Override
	public void onTeamsSet(LinkedList<Team> teamList) {
		LinkedList<Team> teams = new LinkedList<Team>(getInfo().getTeams());
		if (teams.size() > index) {
			setTeam(teams.get(index));
		} else {
			setTeam(null);
		}
	}

	@Override
	public void onPlayerLeft(int ownerId) {
		recalculate();
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = 150;
		if (index > 0) {
			screenX = newWidth - width;
		}
	}

}
