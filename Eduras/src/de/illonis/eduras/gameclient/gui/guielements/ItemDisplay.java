package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.ImageList;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Displays player items on user interface.
 * 
 * @author illonis
 * 
 */
public class ItemDisplay extends ClickableGuiElement implements
		TooltipTriggerer {

	private final static int ITEM_GAP = 10;
	// top, right, bottom, left
	private final static int OUTER_GAP[] = { 20, 5, 10, 15 };
	private int height, width, blocksize, itemGap;
	private GuiItem itemSlots[];
	private ImageList images;

	public ItemDisplay(GuiClickReactor gui, InformationProvider info,
			ImageList images) {
		super(gui, info);
		this.images = images;
		width = 140;
		blocksize = 30;
		itemGap = ITEM_GAP;
		height = OUTER_GAP[0] + 2 * blocksize + OUTER_GAP[2] + ITEM_GAP;
		itemSlots = new GuiItem[Inventory.MAX_CAPACITY];
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			itemSlots[i] = new GuiItem(i);
		}
	}

	@Override
	public void render(Graphics2D g2d, ImageList imageList) {
		g2d.setColor(Color.GRAY);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.BLACK);
		for (GuiItem item : itemSlots) {
			// TODO: make nicer
			g2d.drawRect(item.getX() + screenX, item.getY() + screenY,
					blocksize, blocksize);
			g2d.drawString("#" + item.getSlotId() + ": " + item.getName(),
					item.getX() + screenX, item.getY() + screenY);
			if (item.hasImage())
				g2d.drawImage(item.getItemImage(), item.getX() + screenX,
						item.getY() + screenY, null);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = newHeight - height;
	}

	@Override
	public boolean onClick(Point p) {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			if (itemSlots[i].getClickableRect().contains(p)) {
				EduLog.info("User clicked on item " + i);
				itemClicked(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Indicates that an item has been clicked.
	 * 
	 * @param i
	 *            item clicked.
	 */
	private void itemClicked(int i) {
		try {
			if (getInfo().getPlayer().getInventory().isItemInSlot(i))
				reactor.itemClicked(i);
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
		}
	}

	/**
	 * Called when an item in logic has changed. This will update gui so user
	 * sees up to date item icon.
	 * 
	 * @param slot
	 *            slot that changed.
	 */
	public void onItemChanged(int slot) {
		String newName;
		try {
			Item item = getInfo().getPlayer().getInventory()
					.getItemBySlot(slot);
			newName = item.getName();
			if (images.hasImageFor(item)) {
				itemSlots[slot].setItemImage(images.getItemFor(item));
			}
		} catch (ItemSlotIsEmptyException e) {
			newName = "E";
		} catch (ObjectNotFoundException e) {
			newName = "N/A";
			EduLog.error("Item slot was created in gui, but assigned player was not found.");
		}
		itemSlots[slot].setName(newName);
	}

	/**
	 * A single gui item that is clickable.
	 * 
	 * @author illonis
	 * 
	 */
	private class GuiItem implements ClickableElement {
		private int x, y, slotId;
		private String name;
		private BufferedImage itemImage;

		GuiItem(int slotId) {

			this.x = OUTER_GAP[3] + (blocksize + itemGap)
					* (slotId % (Inventory.MAX_CAPACITY / 2));
			this.y = OUTER_GAP[0] + (blocksize + itemGap)
					* (slotId / (Inventory.MAX_CAPACITY / 2));
			this.slotId = slotId;
			setName("?");
		}

		public boolean hasImage() {
			return itemImage != null;
		}

		BufferedImage getItemImage() {
			return itemImage;
		}

		void setItemImage(BufferedImage itemImage) {
			this.itemImage = itemImage;
		}

		int getX() {
			return x;
		}

		int getY() {
			return y;
		}

		int getSlotId() {
			return slotId;
		}

		String getName() {
			return name;
		}

		void setName(String name) {
			this.name = name;
		}

		@Override
		public Rectangle getClickableRect() {
			return new Rectangle(x + screenX, y + screenY, blocksize, blocksize);
		}
	}

	@Override
	public void onPlayerInformationReceived() {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			onItemChanged(i);
		}
	}

	@Override
	public void onMouseAt(Point p) {
		if (new Rectangle(screenX, screenY, width, height).contains(p)) {
			for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
				if (itemSlots[i].getClickableRect().contains(p)) {
					try {
						reactor.showItemTooltip(p, getInfo().getPlayer()
								.getInventory().getItemBySlot(i));
						return;
					} catch (ItemSlotIsEmptyException e) {

					} catch (ObjectNotFoundException e) {

					}
				}
			}
		}
		reactor.hideTooltip();
	}
}
