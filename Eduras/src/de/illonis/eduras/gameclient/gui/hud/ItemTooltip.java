package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.items.Item;

/**
 * Shows information concerning an item.
 * 
 * @author illonis
 * 
 */
public class ItemTooltip extends Tooltip {

	private final static Logger L = EduLog.getLoggerFor(ItemTooltip.class
			.getName());

	private Item item;

	/**
	 * Creates a new item tooltip that uses given information and displays data
	 * about given item.
	 * 
	 * @param gui
	 *            user interface.
	 * @param item
	 *            item to present.
	 */
	public ItemTooltip(UserInterface gui, Item item) {
		super(gui);
		width = 200;
		height = 100;
		this.item = item;
	}

	@Override
	public void render(Graphics g2d) {
		g2d.setLineWidth(1f);
		g2d.setColor(Color.black);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.gray);
		g2d.drawRect(screenX, screenY, width, height);
		g2d.setColor(Color.yellow);
		g2d.drawString(item.getName(), screenX + 8, screenY + 10);
		g2d.setColor(Color.white);
		g2d.drawString("id: " + item.getId(), screenX + 8, screenY + 50);
		g2d.drawString("owner: " + item.getOwner(), screenX + 8, screenY + 70);
		Image img;
		try {
			img = ImageCache.getInventoryIcon(item.getType());
			g2d.drawImage(img, screenX + width - img.getWidth() - 2,
					screenY + 2);
		} catch (CacheException e) {
			L.log(Level.SEVERE,
					"Could not find cache image for " + item.getType(), e);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {

	}

	@Override
	public void onPlayerInformationReceived() {

	}

	/**
	 * Changes item that should be displayed.
	 * 
	 * @param item
	 *            new item.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

}
