package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.UserInterface;

/**
 * A time frame that shows remaining time.
 * 
 * @author illonis
 * 
 */
public class TimeFrame extends RenderedGuiObject {
	private final static int WIDTH = 40;
	private final static int HEIGHT = 30;

	/**
	 * Creates a new time frame.
	 * 
	 * @param gui
	 *            associated gui.
	 */
	public TimeFrame(UserInterface gui) {
		super(gui);
		setVisibleForSpectator(true);
		screenX = 0;
		screenY = 0;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setFont(DEFAULT_FONT);
		String timeString = getRemainingTimeString();
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(timeString);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(screenX, screenY, WIDTH, HEIGHT);
		g2d.setColor(Color.BLACK);
		g2d.drawString(getRemainingTimeString(), screenX + WIDTH - textWidth
				- 5, screenY + 20);
	}

	private String getRemainingTimeString() {
		long remaining = getInfo().getRemainingTime() / 1000;
		int minutes = (int) (remaining / 60);
		int seconds = (int) (remaining - minutes * 60);
		return String.format("%d:%02d", minutes, seconds);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - WIDTH;
	}

	@Override
	public void onPlayerInformationReceived() {
	}
}
