package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;

/**
 * Displays a statistics frame that displays player stats of all players.
 * 
 * @author illonis
 * 
 */
public class StatisticsWindow extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(StatisticsWindow.class
			.getName());

	private final static Color COLOR_BG = new Color(0, 0, 0, 200);
	private final static Color COLOR_TEXT = Color.white;
	private final static Color COLOR_HEADER = Color.yellow;
	private final static int[] COLUMN_X = { 80, 180, 280 };
	private final static int PADDING_Y = 80;
	private final static int LINEHEIGHT = 30;
	private final static long DISPLAY_TIME = 3000;

	private final int width, height;
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
		width = 400;
		height = 300;
	}

	/**
	 * Changes visibility of this window.
	 * 
	 * @param visible
	 *            true if window should be visible.
	 * 
	 * @author illonis
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void render(Graphics g2d) {
		if (!visible)
			return;
		// background
		g2d.setColor(COLOR_BG);
		Image artwork = null;
		try {
			artwork = ImageCache.getGuiImage(ImageKey.STATISTICS_BG);
		} catch (CacheException e) {
			L.log(Level.SEVERE, "error loading statwindow background", e);
		}
		if (artwork != null)
			g2d.drawImage(artwork, screenX, screenY, null);
		else
			g2d.fillRect(screenX, screenY, width, height);

		// header
		g2d.setColor(COLOR_HEADER);
		g2d.drawString("Player", screenX + COLUMN_X[0], screenY + PADDING_Y);
		g2d.drawString("Kills", screenX + COLUMN_X[1], screenY + PADDING_Y);
		g2d.drawString("Deaths", screenX + COLUMN_X[2], screenY + PADDING_Y);
		// players
		g2d.setColor(COLOR_TEXT);
		int i = 1;
		for (Team team : getInfo().getTeams()) {
			drawTeamRow(g2d, team, i++);
			for (Player p : team.getPlayers()) {
				drawPlayerRow(g2d, p, i++);
			}
		}
	}

	private void drawTeamRow(Graphics g2d, Team team, int i) {
		g2d.setColor(Color.white);
		g2d.drawString(team.getName(), screenX + COLUMN_X[0] - 50, screenY
				+ PADDING_Y + i * LINEHEIGHT);

	}

	private void drawPlayerRow(Graphics g2d, Player p, int i) {
		// name
		g2d.setColor(Color.yellow);
		g2d.drawString(p.getName(), screenX + COLUMN_X[0], screenY + PADDING_Y
				+ i * LINEHEIGHT);

		// deaths
		g2d.drawString(getInfo().getStatistics().getKillsOfPlayer(p) + "",
				screenX + COLUMN_X[1], screenY + PADDING_Y + i * LINEHEIGHT);

		// kills
		g2d.drawString(getInfo().getStatistics().getDeathsOfPlayer(p) + "",
				screenX + COLUMN_X[2], screenY + PADDING_Y + i * LINEHEIGHT);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		// change position so window is centered again
		screenX = (newWidth - width) / 2;
		screenY = (newHeight - height) / 2;
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
