package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gamemodes.TeamDeathmatch;
import de.illonis.eduras.units.InteractMode;

/**
 * Displays a statistics frame that displays player stats of all players.
 * 
 * @author illonis
 * 
 */
public class StatisticsWindow extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(StatisticsWindow.class
			.getName());

	private final static Color COLOR_TEXT = Color.yellow;
	private final static Color COLOR_TEAM = Color.white;
	private final static Color COLOR_HEADER = Color.yellow;
	private int[] xPositions = new int[4];
	private int topInset;
	private final static long DISPLAY_TIME = 3000;
	private Image artwork;
	private Font font, largeFont;
	private int lineHeight;
	private int sideInset;

	private int width, height;
	private boolean visible;

	/**
	 * Creates a new statistics window.
	 * 
	 * @param gui
	 *            parent gui.
	 */
	public StatisticsWindow(UserInterface gui) {
		super(gui);
		visible = false;
		screenX = 0;
		screenY = 0;
	}

	/**
	 * Changes visibility of this window.
	 * 
	 * @param visible
	 *            true if window should be visible.
	 * 
	 * @author illonis
	 */
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void render(Graphics g2d) {
		if (!visible)
			return;
		if (artwork == null) {
			try {
				largeFont = FontCache.getFont(FontKey.BIG_FONT, g2d);
				font = FontCache.getFont(FontKey.DEFAULT_FONT, g2d);
				lineHeight = font.getLineHeight() + 5;
				artwork = ImageCache.getGuiImage(ImageKey.STATISTICS_BG);
				width = artwork.getWidth();
				height = artwork.getHeight();
				sideInset = Math.round(10 * GameRenderer.getRenderScale());
				screenX = (Display.getWidth() - width) / 2;
				screenY = (Display.getHeight() - height) / 2;
				topInset = Math.round(60 * GameRenderer.getRenderScale());
				int statusColumn = width - sideInset
						- font.getWidth(InteractMode.MODE_STRATEGY.name());
				int killsdeathColumnWidth = largeFont.getWidth("Deaths");
				xPositions = new int[] { sideInset,
						statusColumn - killsdeathColumnWidth * 2 - sideInset,
						statusColumn - killsdeathColumnWidth - sideInset,
						statusColumn };

			} catch (CacheException e) {
				L.log(Level.SEVERE, "error loading statwindow background", e);
				return;
			}
		}
		g2d.drawImage(artwork, screenX, screenY);
		drawHeader();

		// header
		largeFont.drawString(screenX + xPositions[0], screenY + topInset,
				"Player", COLOR_HEADER);
		largeFont.drawString(screenX + xPositions[1], screenY + topInset,
				"Kills", COLOR_HEADER);
		largeFont.drawString(screenX + xPositions[2], screenY + topInset,
				"Deaths", COLOR_HEADER);
		largeFont.drawString(screenX + xPositions[3], screenY + topInset,
				"Status", COLOR_HEADER);
		// players
		float y = screenY + topInset + lineHeight;
		for (Team team : getInfo().getTeams()) {
			y += 10;
			drawTeamRow(team, y);
			for (Player p : team.getPlayers()) {
				y += lineHeight;
				drawPlayerRow(p, y);
			}
			y += lineHeight;
		}
	}

	private void drawHeader() {
		String state = "";
		if (getInfo().getGameMode() instanceof TeamDeathmatch) {
			List<Team> teams = new LinkedList<Team>(getInfo().getTeams());
			if (teams.size() == 2) {
				state = teams.get(0).getName()
						+ "  "
						+ getInfo().getStatistics()
								.getKillsByTeam(teams.get(0))
						+ " : "
						+ getInfo().getStatistics()
								.getKillsByTeam(teams.get(1)) + "  "
						+ teams.get(1).getName();
			}
		}

		largeFont.drawString(
				screenX + sideInset + (width - largeFont.getWidth(state)) / 2,
				screenY + (topInset - largeFont.getLineHeight()) / 2, state,
				COLOR_HEADER);
	}

	private void drawTeamRow(Team team, float y) {
		font.drawString(screenX + xPositions[0] - 5, y, team.getName(),
				team.getColor());
	}

	private void drawPlayerRow(Player p, float y) {
		// name
		font.drawString(screenX + xPositions[0], y, p.getName(), COLOR_TEXT);

		// deaths
		font.drawString(screenX + xPositions[1], y, getInfo().getStatistics()
				.getKillsOfPlayer(p) + "", COLOR_TEXT);

		// kills
		font.drawString(screenX + xPositions[2], y, getInfo().getStatistics()
				.getDeathsOfPlayer(p) + "", COLOR_TEXT);

		// player's status, only show to own team
		String status = "unknown";
		try {
			if (p.getTeam().equals(getInfo().getPlayer().getTeam())) {
				status = p.getCurrentMode() + "";
			}
		} catch (PlayerHasNoTeamException | ObjectNotFoundException e) {
			L.log(Level.WARNING, "Problem when rendering the stats window!", e);
		}
		font.drawString(screenX + xPositions[3], y, status, COLOR_TEXT);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		// change position so window is centered again

	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		Thread t = new Thread(delayedHider);
		t.setName("DelayedHider");
		setVisible(true);
		t.start();
		super.onMatchEnd(event);
	}

	private final Runnable delayedHider = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(DISPLAY_TIME);
			} catch (InterruptedException e) {
				L.log(Level.SEVERE, "Interrupted when sleeping in delayHider.",
						e);
			}
			setVisible(false);
		}
	};
}
