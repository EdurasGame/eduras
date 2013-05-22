package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;

import de.illonis.eduras.gameclient.gui.UserInterface;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.units.Player;

/**
 * Displays a statistics frame.
 * 
 * @author illonis
 * 
 */
public class StatisticsWindow extends RenderedGuiObject {

	private final static Color COLOR_BG = new Color(0, 0, 0, 200);
	private final static Color COLOR_TEXT = Color.WHITE;
	private final static Color COLOR_HEADER = Color.YELLOW;
	private final static int BORDERSIZE = 100;
	private final static int LINEHEIGHT = 30;
	private BufferedImage artwork;
	private Collection<Player> players;
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
		screenX = BORDERSIZE;
		screenY = BORDERSIZE;
		width = 400;
		height = 300;
		try {
			artwork = ImageFiler.load("gui/artwork/statwindow.png");
		} catch (IOException e) {
			EduLog.passException(e);
		}
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
	public void render(Graphics2D g2d) {
		if (!visible)
			return;
		// background
		g2d.setPaint(COLOR_BG);
		//g2d.fillRect(screenX, screenY, width, height);
		g2d.drawImage(artwork, screenX, screenY, null);

		// header
		g2d.setColor(COLOR_HEADER);
		g2d.drawString("Player", screenX + 50, screenY + 50);
		g2d.drawString("Kills", screenX + 150, screenY + 50);
		// players
		g2d.setColor(COLOR_TEXT);
		int i = 1;
		for (Player p : players) {
			g2d.drawString(p.getName(), screenX + 50, screenY + 50 + i
					* LINEHEIGHT);
			g2d.drawString(getInfo().getStatistics().getKillsOfPlayer(p) + "",
					screenX + 150, screenY + 50 + i * LINEHEIGHT);
			i++;
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = (newWidth - width) / 2;
		screenY = (newHeight - height) / 2;

		// width = newWidth - BORDERSIZE * 2;
		// height = newHeight - BORDERSIZE * 2;
	}

	@Override
	public void onPlayerInformationReceived() {
		players = getInfo().getPlayers();
	}
}
