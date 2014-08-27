package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;

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
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g2d);
		String timeString = getRemainingTimeString();
		int textWidth = font.getWidth(timeString);
		g2d.setColor(stringColor);
		font.drawString(screenX + WIDTH - textWidth - 5, screenY + yOffset,
				getRemainingTimeString());
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
