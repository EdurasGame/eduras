package de.illonis.eduras.gameclient.datacache;

import java.util.HashMap;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

/**
 * A cache that holds all fonts.
 * 
 * @author illonis
 * 
 */
public class FontCache {

	/**
	 * Font identifier.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum FontKey {
		TOOLTIP_FONT, DEFAULT_FONT, BIG_FONT, SMALL_FONT, HUGE_FONT, CHAT_FONT, NAMEPLATE;
	}

	private final static HashMap<FontKey, TrueTypeFont> fontData = new HashMap<FontKey, TrueTypeFont>();

	static void addFont(FontKey key, TrueTypeFont font) {
		fontData.put(key, font);
	}

	/**
	 * Retrieves a font by given key.
	 * 
	 * @param key
	 *            the key for that font.
	 * @return the cached font.
	 * @throws CacheException
	 *             if font is not cached.
	 */
	public static Font getFont(FontKey key) throws CacheException {
		TrueTypeFont font = fontData.get(key);
		if (font == null)
			throw new CacheException("No cached font found for " + key);
		return font;
	}

	/**
	 * Retrieves a font by given key. If the font is not found, the default font
	 * is used.
	 * 
	 * @param key
	 *            the key for that font.
	 * @param g
	 *            the graphics that's default font should be used.
	 * @return the cached font or the default font if cached font not found.
	 */
	public static Font getFont(FontKey key, Graphics g) {
		TrueTypeFont font = fontData.get(key);
		if (font == null)
			return g.getFont();
		return font;
	}
}
