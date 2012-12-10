package de.illonis.eduras.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.InformationProvider;

public class ItemDisplay extends RenderedGuiObject {

	private int height, width, blocksize, itemGap;
	private GuiItem itemSlots[];

	public ItemDisplay(InformationProvider info) {
		super(info);

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
			// TODO: make nicerok
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

	private class GuiItem {
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
	}
}
