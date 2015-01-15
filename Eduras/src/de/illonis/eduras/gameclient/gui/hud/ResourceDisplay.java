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
	protected Image icon;
	private Font font;
	private float textX;
	protected float iconY;

	protected ResourceDisplay(UserInterface gui) {
		super(gui);
		resAmount = 0;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(icon, screenX, iconY);
		font.drawString(textX, screenY, resAmount + "", Color.white);
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		try {
			icon = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON);
		} catch (CacheException e) {
			L.log(Level.SEVERE, "Resource icon not found.", e);
			return false;
		}
		iconY = screenY + (font.getLineHeight() - icon.getHeight()) / 2;
		textX = screenX + icon.getWidth() + 3;
		return true;
	}
}
