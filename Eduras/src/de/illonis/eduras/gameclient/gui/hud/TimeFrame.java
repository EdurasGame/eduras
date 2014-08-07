package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * A time frame that shows some indication of time on the right side of the gui
 * in a minutes:seconds format.
 * 
 * @author illonis / renmai
 * 
 */
public abstract class TimeFrame extends RenderedGuiObject {
	private final static int WIDTH = 40;

	private final float yOffset;
	private final Color stringColor;

	/**
	 * Creates a new time frame.
	 * 
	 * @param gui
	 *            associated gui.
	 * @param yOffset
	 *            the offset of y position where to draw the time-string
	 */
	protected TimeFrame(UserInterface gui, Color stringColor, float yOffset) {
		super(gui);
		setVisibleForSpectator(true);
		screenX = 0;
		screenY = 0;
		this.yOffset = yOffset;
		this.stringColor = stringColor;
	}

	@Override
	public void render(Graphics g2d) {

		String timeString = getRemainingTimeString();
		int textWidth = g2d.getFont().getWidth(timeString);
		g2d.setColor(stringColor);
		g2d.drawString(getRemainingTimeString(), screenX + WIDTH - textWidth
				- 5, screenY + yOffset);
	}

	private String getRemainingTimeString() {
		long remaining = getTimeToDisplay() / 1000;
		int minutes = (int) (remaining / 60);
		int seconds = (int) (remaining - minutes * 60);
		return String.format("%d:%02d", minutes, seconds);
	}

	protected abstract long getTimeToDisplay();

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - WIDTH;
	}
}
