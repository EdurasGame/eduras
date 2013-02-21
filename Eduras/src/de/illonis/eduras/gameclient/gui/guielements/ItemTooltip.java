package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.illonis.eduras.gameclient.gui.ImageList;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Shows information concerning an item.
 * 
 * @author illonis
 * 
 */
public class ItemTooltip extends Tooltip {

	private Item item;

	/**
	 * Creates a new item tooltip that uses given information and displays data
	 * about given item.
	 * 
	 * @param info
	 *            game information.
	 * @param item
	 *            item to present.
	 */
	public ItemTooltip(InformationProvider info, Item item) {
		super(info);
		width = 200;
		height = 100;
		this.item = item;
	}

	@Override
	public void render(Graphics2D g2d, ImageList imageList) {
		super.render(g2d, imageList);
		g2d.setColor(Color.YELLOW);
		g2d.drawString(item.getName(), screenX + 8, screenY + 20);
		g2d.setColor(Color.WHITE);
		g2d.drawString(item.getType().toString(), screenX + 8, screenY + 40);
		g2d.drawString("id: " + item.getId(), screenX + 8, screenY + 70);
		g2d.drawString("owner: " + item.getOwner(), screenX + 8, screenY + 90);
		BufferedImage img = imageList.getItemFor(item);
		g2d.drawImage(img, screenX + width - img.getWidth() - 2, screenY + 2,
				null);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {

	}

	@Override
	public void onPlayerInformationReceived() {

	}

	public Item getItem() {
		return item;
	}

}
