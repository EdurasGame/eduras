package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic;
import de.illonis.eduras.Statistic.PlayerStatEntry;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.RoundEndEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gamemodes.Edura;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gamemodes.TeamDeathmatch;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.settings.S;
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
	private final static Color COLOR_HEADER = Color.yellow;
	private final static Color COLOR_HIGHLIGHT = new Color(1f, 1f, 1f, 0.3f);
	private int[] xPositions = new int[4];
	private int topInset;
	private Image artwork;
	private Font font, largeFont;
	private int lineHeight;
	private int sideInset;
	private boolean useStored;
	private Statistic storedStats;
	private LinkedList<Team> storedTeams;

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
		useStored = false;
		zIndex = 5;
		visibleForSpectator = true;
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		Player p;
		try {
			p = getInfo().getPlayerByOwnerId(ownerId);
			getInfo().getStatistics().addPlayerToStats(p);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Could not add joined player to stats.", e);
		}

	}

	@Override
	public void onPlayerLeft(int ownerId) {
		getInfo().getStatistics().removePlayerFromStats(ownerId);
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
		if (!visible) {
			useStored = false;
		}
	}

	@Override
	public void render(Graphics g2d) {
		if (!visible)
			return;
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

		List<PlayerStatEntry> entries = new LinkedList<PlayerStatEntry>(
				getStats().getStatList());

		if (getInfo().getGameMode().getNumber() == GameModeNumber.DEATHMATCH) {
			for (PlayerStatEntry p : entries) {
				y += lineHeight;
				drawPlayerRow(p, g2d, y);
			}
		} else {
			for (Team team : getInfo().getTeams()) {
				if (getInfo().getGameMode().getNumber() != GameModeNumber.DEATHMATCH) {
					y += 10;
					drawTeamRow(team, y);
				}

				for (PlayerStatEntry p : entries) {
					try {
						if (p.getPlayer().getTeam().equals(team)) {
							y += lineHeight;
							drawPlayerRow(p, g2d, y);
						}
					} catch (PlayerHasNoTeamException e) {
						L.log(Level.SEVERE, "Found a player without team.", e);
					}
				}
				y += lineHeight;
			}
		}
	}

	private void drawHeader() {
		String state = "";
		GameMode gameMode = getInfo().getGameMode();
		if (gameMode instanceof TeamDeathmatch && !(gameMode instanceof Edura)) {
			if (getTeams().size() == 2) {
				state = putTeamScores(
						getStats().getKillsByTeam(getTeams().get(0)),
						getStats().getKillsByTeam(getTeams().get(1)));
			}
		} else {
			if (gameMode instanceof Edura && getTeams().size() == 2) {
				int score1 = getStats().getScoreOfTeam(getTeams().get(0));
				int score2 = getStats().getScoreOfTeam(getTeams().get(1));
				state = putTeamScores(score1, score2);
				int currentRound = 1
						+ getStats().getScoreOfTeam(getTeams().get(0))
						+ getStats().getScoreOfTeam(getTeams().get(1));
				String roundText = Localization.getStringF(
						"client.gui.stats.roundprogress", currentRound,
						S.Server.gm_edura_maxrounds);
				font.drawString(
						screenX + sideInset
								+ (width - font.getWidth(roundText)) / 2,
						screenY + (topInset - largeFont.getLineHeight()) / 2
								+ largeFont.getLineHeight(), roundText,
						Color.white);
			} else {
				state = getInfo().getGameMode().getName();
			}
		}

		largeFont.drawString(
				screenX + sideInset + (width - largeFont.getWidth(state)) / 2,
				screenY + (topInset - largeFont.getLineHeight()) / 2, state,
				COLOR_HEADER);

	}

	private String putTeamScores(int scoreOfTeam, int scoreOfTeam2) {
		return getTeams().get(0).getName() + "  " + scoreOfTeam + " : "
				+ scoreOfTeam2 + "  " + getTeams().get(1).getName();
	}

	private void drawTeamRow(Team team, float y) {
		font.drawString(screenX + xPositions[0] - 5, y, team.getName(),
				team.getColor());
	}

	private void drawPlayerRow(PlayerStatEntry data, Graphics g2d, float y) {
		Player me = null;
		if (getInfo().getClientData().getRole() != ClientRole.SPECTATOR) {
			try {
				me = getInfo().getPlayer();
			} catch (ObjectNotFoundException e1) {
				L.log(Level.WARNING,
						"Own player not found while rendering stats.", e1);
			}
		}
		if (data.getPlayer().equals(me)) {
			g2d.setColor(COLOR_HIGHLIGHT);
			g2d.fillRect(screenX + sideInset, y, width - 2 * sideInset,
					font.getLineHeight());
		}
		// name
		font.drawString(screenX + xPositions[0], y, data.getPlayer().getName(),
				COLOR_TEXT);

		// deaths
		font.drawString(screenX + xPositions[1], y,
				data.getProperty(StatsProperty.KILLS) + "", COLOR_TEXT);

		// kills
		font.drawString(screenX + xPositions[2], y,
				data.getProperty(StatsProperty.DEATHS) + "", COLOR_TEXT);

		// player's status, only show to own team
		String status = "";
		if (!useStored) {
			try {
				if (getInfo().getClientData().getRole() == ClientRole.SPECTATOR
						|| data.getPlayer().getTeam().equals(me.getTeam())) {
					status = data.getPlayer().getCurrentMode().getDisplayName();
				}
			} catch (PlayerHasNoTeamException e) {
				L.log(Level.WARNING,
						"Could not determine current players team while showing stats",
						e);
			}
		}
		font.drawString(screenX + xPositions[3], y, status, COLOR_TEXT);
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		try {
			largeFont = FontCache.getFont(FontKey.BIG_FONT, g);
			font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
			lineHeight = font.getLineHeight() + 5;
			artwork = ImageCache.getGuiImage(ImageKey.STATISTICS_BG);
			width = artwork.getWidth();
			height = artwork.getHeight();
			sideInset = Math.round(10 * GameRenderer.getRenderScale());
			screenX = (windowWidth - width) / 2;
			screenY = (windowHeight - height) / 2;
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
			return false;
		}
		return true;
	}

	@Override
	public void onRoundEnd(RoundEndEvent event) {
		Thread t = new Thread(delayedHider);
		t.setName("DelayedStatHider");
		storedStats = getInfo().getStatistics().copy();
		storedTeams = new LinkedList<Team>(getInfo().getTeams());
		useStored = true;
		setVisible(true);
		t.start();
	}

	private final Runnable delayedHider = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(S.Server.sv_game_restart_delay);
			} catch (InterruptedException e) {
				L.log(Level.SEVERE, "Interrupted when sleeping in delayHider.",
						e);
			}
			useStored = false;
			setVisible(false);
		}
	};

	private LinkedList<Team> getTeams() {
		if (useStored) {
			return storedTeams;
		} else
			return new LinkedList<Team>(getInfo().getTeams());
	}

	private Statistic getStats() {
		if (useStored) {
			return storedStats;
		} else
			return getInfo().getStatistics();
	}
}
