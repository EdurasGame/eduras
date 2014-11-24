package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.RoundEndEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.gui.hud.nifty.GameState;
import de.illonis.eduras.gamemodes.Deathmatch;
import de.illonis.eduras.gamemodes.TeamDeathmatch;

/**
 * Displays a single line of text very prominent on the screen.
 * 
 * @author illonis
 * 
 */
public class BigPanel extends RenderedGuiObject {
	private final static Logger L = EduLog.getLoggerFor(BigPanel.class
			.getName());

	private final static long DEFAULT_DISPLAY_TIME = 5000;
	private String message;
	private long step, last, remaining;
	private int width;
	private float alpha;
	private Font font;
	private boolean fontLoaded = false;

	/**
	 * @param gui
	 *            the gui.
	 */
	public BigPanel(UserInterface gui) {
		super(gui);
		message = "";
		setVisibleForSpectator(true);
		last = System.currentTimeMillis();
		font = GameState.getBigNoteFont();
	}

	@Override
	public void render(Graphics g2d) {
		if (!fontLoaded) {
			fontLoaded = true;
			font = FontCache.getFont(FontKey.HUGE_FONT, g2d);
		}
		checkRemaining();
		if (!message.isEmpty()) {
			g2d.setColor(new Color(1f, 1f, 1f, alpha));
			font.drawString(screenX, screenY, message);
		}
	}

	/**
	 * Sets the message that should be displayed. Each message shown for
	 * {@value #DEFAULT_DISPLAY_TIME}ms. Setting a new message immediately
	 * disposes the former one.
	 * 
	 * @param text
	 *            the message.
	 */
	public void setMessage(String text) {
		this.message = text;
		remaining = DEFAULT_DISPLAY_TIME;
		last = System.currentTimeMillis();
		int w = font.getWidth(message);
		screenX = (width - w) / 2;
		alpha = 1f;
	}

	private void checkRemaining() {
		if (message.isEmpty())
			return;
		step = System.currentTimeMillis() - last;
		if (remaining <= 0) {
			if (alpha <= 0.1f) {
				message = "";
			} else {
				alpha = Math.max(0f, alpha - 0.01f);
			}
		} else {
			remaining -= step;
		}
		last = System.currentTimeMillis();
	}

	@Override
	public void onDeath(DeathEvent event) {
		message = "";
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		int winnerId = event.getWinnerId();

		announceWinner("Match", winnerId);
	}

	@Override
	public void onRoundEnd(RoundEndEvent event) {
		int winnerId = event.getWinnerId();

		announceWinner("Round", winnerId);
	}

	private void announceWinner(String winnerOfWhat, int winnerId) {

		if (winnerId == -1) {
			setMessage(winnerOfWhat + " Draw");
			return;
		}

		if (getInfo().getGameMode() instanceof TeamDeathmatch) {
			Team team = getInfo().findTeamById(winnerId);
			if (team != null) {
				setMessage(team.getName() + " won " + winnerOfWhat);
			}
		} else if (getInfo().getGameMode() instanceof Deathmatch) {
			try {
				Player p = getInfo().getPlayerByOwnerId(winnerId);
				setMessage(p.getName() + " won " + winnerOfWhat);
			} catch (ObjectNotFoundException e) {
				L.log(Level.WARNING, "Could not find winning player", e);
			}
		} else {
			System.out.println("other mode??"
					+ getInfo().getGameMode().getName());
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		this.width = newWidth;
		screenY = newHeight / 3;
	}

}
