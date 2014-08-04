package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.jcraft.jorbis.Info;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
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
	private final static Logger L = EduLog.getLoggerFor(BigPanel.class.getName());

	private final static int Y_INSET = 150;
	private final static long DEFAULT_DISPLAY_TIME = 5000;
	private String message;
	private long step, last, remaining;
	private int width;
	private float alpha;

	/**
	 * @param gui
	 *            the gui.
	 */
	public BigPanel(UserInterface gui) {
		super(gui);
		message = "";
		setVisibleForSpectator(true);
		last = System.currentTimeMillis();
	}

	@Override
	public void render(Graphics g2d) {
		checkRemaining();
		if (!message.isEmpty()) {
			g2d.setColor(new Color(1f, 1f, 1f, alpha));
			g2d.setFont(GameState.getBigNoteFont());
			g2d.drawString(message, screenX, Y_INSET);
			g2d.setFont(GameState.getDefaultFont());
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
		int w = GameState.getBigNoteFont().getWidth(message);
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
		if (getInfo().getGameMode() instanceof TeamDeathmatch) {
			Team team = getInfo().findTeamById(event.getWinnerId());
			if (team != null) {
				setMessage(team.getName() + " won");
			}
		} else if (getInfo().getGameMode() instanceof Deathmatch) {
			try {
				Player p = getInfo().getPlayerByOwnerId(event.getWinnerId());
				setMessage(p.getName() + " won");
			} catch (ObjectNotFoundException e) {
				L.log(Level.WARNING, "Could not find winning player", e);
			}
		} else {
			System.out.println("other mode??" + getInfo().getGameMode().getName());
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		this.width = newWidth;
	}

}
