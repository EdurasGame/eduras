package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;

/**
 * Displays team resources.
 * 
 * @author illonis
 * 
 */
public abstract class ResourceDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(ResourceDisplay.class
			.getName());

	protected int resAmount;

	protected ResourceDisplay(UserInterface gui) {
		super(gui);
		resAmount = 0;
	}

	@Override
	public void render(Graphics g) {
		Image i = null;
		try {
			i = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Resource icon not found.", e);
		}
		if (i == null)
			return;
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		g.drawImage(i, screenX, screenY
				+ (font.getLineHeight() - i.getHeight()) / 2);
		g.setColor(Color.white);
		font.drawString(screenX + i.getWidth() + 3, screenY, resAmount + "");
	}
}
