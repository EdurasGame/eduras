package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;

/**
 * A minimap.
 * 
 * @author illonis
 * 
 */
public class MiniMap extends ClickableGuiElement {

	final static int SIZE = 150;
	private final Rectangle bounds;

	protected MiniMap(UserInterface gui) {
		super(gui);
		bounds = new Rectangle(0, 0, SIZE, SIZE);
	}

	private final static Logger L = EduLog
			.getLoggerFor(MiniMap.class.getName());

	@Override
	public boolean onClick(Vector2f p) {
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
		try {
			Image i = ImageCache.getGuiImage(ImageKey.MINIMAP_DUMMY);
			g.drawImage(i, screenX, screenY);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Minimap dummy image not found.", e);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - SIZE;
		bounds.setLocation(screenX, screenY);
	}

}
