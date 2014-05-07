package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * A time frame that shows remaining time.
 * 
 * @author illonis
 * 
 */
public class TimeFrame extends RenderedGuiObject {
	private final static int WIDTH = 40;

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
	public void render(Graphics g2d) {

		String timeString = getRemainingTimeString();
		int textWidth = g2d.getFont().getWidth(timeString);
		g2d.setColor(Color.white);
		g2d.drawString(getRemainingTimeString(), screenX + WIDTH - textWidth
				- 5, screenY + 10);
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
}
