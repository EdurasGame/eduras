package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Displays a single line of text above the action bar to provide some hints.
 * 
 * @author illonis
 * 
 */
public class TipPanel extends RenderedGuiObject {

	private final static int Y_INSET = 50;
	private final static long DEFAULT_DISPLAY_TIME = 2000;
	private int y = 0;
	private final int x = MiniMap.SIZE + 20;
	private String message;
	private long step, last, remaining;

	/**
	 * @param gui
	 *            the gui.
	 */
	public TipPanel(UserInterface gui) {
		super(gui);
		message = "";
		setVisibleForSpectator(false);
		last = System.currentTimeMillis();
	}

	@Override
	public void render(Graphics g2d) {
		if (!message.isEmpty()) {
			step = last - System.currentTimeMillis();
			remaining -= step;
			g2d.setColor(Color.white);
			g2d.drawString(message, x, y);
			if (remaining <= 0) {
				message = "";
			} else {
				last = System.currentTimeMillis();
			}
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
	public void addMessage(String text) {
		this.message = text;
		remaining = DEFAULT_DISPLAY_TIME;
		last = System.currentTimeMillis();
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		y = newHeight - ActionButton.BUTTON_SIZE - 30 - Y_INSET;
	}

}
