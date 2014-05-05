package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * A textual tooltip.
 * 
 * @author illonis
 * 
 */
public class TextTooltip extends Tooltip {
	private String text;

	/**
	 * Creates a new text-tooltip.
	 * 
	 * @param text
	 *            contained text.
	 */
	public TextTooltip(String text) {
		height = 30;
		width = 100;
		setText(text);
	}

	/**
	 * Updates the text displayed by this tooltip.
	 * 
	 * @param text
	 *            new text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void render(Graphics g2d) {
		super.render(g2d);
		g2d.setColor(Color.yellow);
		g2d.drawString(text, screenX + 8, screenY + 10);
	}

}