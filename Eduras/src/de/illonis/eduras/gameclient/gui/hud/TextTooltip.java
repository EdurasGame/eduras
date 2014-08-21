package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;

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
		Font font;
		try {
			font = FontCache.getFont(FontKey.TOOLTIP_FONT);
			width = 20 + font.getWidth(text);
			height = font.getLineHeight() + 20;
		} catch (CacheException e) {
			width = 250;
		}
		this.text = text;
	}

	@Override
	public void render(Graphics g2d) {
		super.render(g2d);
		g2d.setColor(Color.yellow);
		FontCache.getFont(FontKey.TOOLTIP_FONT, g2d).drawString(screenX + 8,
				screenY + 10, text);
	}

}
