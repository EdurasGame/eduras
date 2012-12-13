package de.illonis.eduras.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Displays player items on user interface.
 * 
 * @author illonis
 * 
 */
public class ItemDisplay extends ClickableGuiElement {

	private int height, width, blocksize, itemGap;
	private GuiItem itemSlots[];

	public ItemDisplay(GuiClickReactor gui, InformationProvider info) {
		super(gui, info);

		width = 150;
		blocksize = 30;
		itemGap = 10;
		height = 20 + 3 * blocksize + 3 * itemGap;
		itemSlots = new GuiItem[Inventory.MAX_CAPACITY];
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			itemSlots[i] = new GuiItem(i);
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.black);
		for (GuiItem item : itemSlots) {
			// TODO: make nicer
			g2d.drawRect(item.getX() + screenX, item.getY() + screenY,
					blocksize, blocksize);
			g2d.drawString("#" + item.getSlotId() + ": " + item.getName(),
					item.getX() + screenX, item.getY() + screenY);
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
			newName = getInfo().getPlayer().getInventory().getItemBySlot(slot)
					.getName();
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

		GuiItem(int slotId) {
			// TODO: Fix item display order.
			this.x = 20 + (blocksize + itemGap)
					* (slotId % (Inventory.MAX_CAPACITY / 2));
			this.y = 20 + (blocksize + itemGap) * (slotId % 2);
			this.slotId = slotId;
			setName("?");
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
}
