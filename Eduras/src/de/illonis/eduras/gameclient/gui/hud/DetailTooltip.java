package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;

/**
 * A tooltip with a title and text.
 * 
 * @author illonis
 * 
 */
public class DetailTooltip extends Tooltip {
	private String title;
	private String text;
	private String costs;
	private final List<String> lines;

	/**
	 * Creates a new tooltip.
	 * 
	 * @param title
	 *            the title
	 * 
	 * @param text
	 *            contained text.
	 */
	public DetailTooltip(String title, String text) {
		height = 30;
		width = 100;
		this.text = "";
		this.title = "";
		lines = new LinkedList<String>();
		set(title, text);
	}

	/**
	 * Updates the text displayed by this tooltip.
	 * 
	 * @param title
	 *            new title
	 * 
	 * @param text
	 *            new text.
	 */
	public void set(String title, String text) {
		if (this.text.equals(text) && this.title.equals(title))
			return;
		lines.clear();
		Font font;
		try {
			font = FontCache.getFont(FontKey.TOOLTIP_FONT);
			width = Math.max(250, 20 + font.getWidth(title));
			lines.addAll(addLinebreaks(text, font));
			height = font.getLineHeight() * (lines.size() + 2) + 20;
		} catch (CacheException e) {
			width = 250;
		}
		this.text = text;
		this.title = title;
	}

	public void setCosts(int costs) {
		if (costs <= 0) {
			this.costs = "";
		}
		this.costs = costs + "";
	}

	public List<String> addLinebreaks(String input, Font font) {
		StringTokenizer tok = new StringTokenizer(input, " ");
		List<String> splitLines = new LinkedList<String>();
		StringBuilder output = new StringBuilder(input.length());
		int lineLen = 0;
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken();

			if (lineLen + font.getWidth(word + " ") > width - 16) {
				splitLines.add(output.toString().trim());
				lineLen = 0;
				output = new StringBuilder(input.length());
			}
			output.append(" " + word);
			lineLen += font.getWidth(" " + word);
		}
		splitLines.add(output.toString().trim());
		return splitLines;
	}

	@Override
	public void render(Graphics g2d) {
		super.render(g2d);
		Font font = FontCache.getFont(FontKey.TOOLTIP_FONT, g2d);
		font.drawString(screenX + 8, screenY + 10, title, Color.yellow);
		for (int i = 0; i < lines.size(); i++) {
			font.drawString(screenX + 8, screenY + font.getLineHeight()
					* (2 + i) + 10 + 5, lines.get(i), Color.white);
		}

		if (!costs.isEmpty()) {
			try {
				Image image = ImageCache
						.getGuiImage(ImageKey.RESOURCE_ICON_SMALL);
				g2d.drawImage(image, screenX + width - font.getWidth(costs) - 7
						- image.getWidth() - 5, screenY + font.getLineHeight()
						+ 12);
			} catch (CacheException e) {
			}
			font.drawString(screenX + width - font.getWidth(costs) - 7, screenY
					+ font.getLineHeight() + 10, costs, Color.white);
		}
	}
}
