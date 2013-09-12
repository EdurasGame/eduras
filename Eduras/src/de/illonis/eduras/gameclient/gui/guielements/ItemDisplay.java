package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.ImageList;
import de.illonis.eduras.gameclient.gui.UserInterface;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.logger.EduLog;

/**
 * Displays player items on user interface.
 * 
 * @author illonis
 * 
 */
public class ItemDisplay extends ClickableGuiElement implements
		TooltipTriggerer {

	private final static int ITEM_GAP = 15;
	private final static int BLOCKSIZE = 48;
	private int currentItem = -1;
	private final static Color COLOR_SEMITRANSPARENT = new Color(0, 0, 0, 120);

	// top, right, bottom, left
	private final static int OUTER_GAP[] = { 20, 5, 10, 15 };

	final static int HEIGHT = OUTER_GAP[0] + 2 * BLOCKSIZE + OUTER_GAP[2]
			+ ITEM_GAP;

	/**
	 * Width of the total inventory.
	 */
	public final static int WIDTH = BLOCKSIZE * 3 + 4 * ITEM_GAP;

	private GuiItem itemSlots[];
	private BasicStroke rectStroke = new BasicStroke(3);

	/**
	 * Creates a new item toolbar.
	 * 
	 * @param gui
	 *            associated gui.
	 */
	public ItemDisplay(UserInterface gui) {
		super(gui);
		itemSlots = new GuiItem[Inventory.MAX_CAPACITY];
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			itemSlots[i] = new GuiItem(i);
		}
		registerAsTooltipTriggerer(this);
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setFont(DEFAULT_FONT);
		g2d.setColor(Color.GRAY);
		g2d.fillRect(screenX, screenY, WIDTH, HEIGHT);
		for (GuiItem item : itemSlots) {
			// TODO: make nicer
			if (item.getSlotId() == currentItem) {
				g2d.setColor(Color.RED);
			} else
				g2d.setColor(Color.BLACK);
			g2d.setStroke(rectStroke);
			Rectangle itemRect = new Rectangle(item.getX() + screenX,
					item.getY() + screenY, BLOCKSIZE, BLOCKSIZE);
			g2d.draw(itemRect);
			g2d.drawString("#" + (item.getSlotId() + 1), item.getX() + screenX
					+ BLOCKSIZE / 4, item.getY() + screenY - 2);
			if (item.hasImage())
				g2d.drawImage(item.getItemImage(), item.getX() + screenX,
						item.getY() + screenY, null);
			long cd = item.getCooldown();
			if (cd > 0) {
				g2d.setPaint(COLOR_SEMITRANSPARENT);
				g2d.fill(new Arc2D.Double(itemRect, 90, item.getCooldownArc(),
						Arc2D.PIE));
			}
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = newHeight - HEIGHT;
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
	 * @param itemSlot
	 *            item clicked.
	 */
	private void itemClicked(int itemSlot) {

		try {
			getInfo().getPlayer().getInventory().getItemBySlot(itemSlot);
			getClickReactor().itemClicked(itemSlot);
			currentItem = itemSlot;

		} catch (ItemSlotIsEmptyException e) {
			EduLog.info("Player tried to use empty item slot " + itemSlot + ".");
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
		}
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		if (event.getOwner() == getInfo().getOwnerID()) {
			Item item = (Item) getInfo().getGameObjects().get(
					event.getObjectId());
			onItemChanged(event.getItemSlot(), item);
		}
	}

	/**
	 * Called when an item in logic has changed. This will update gui so user
	 * sees up to date item icon.
	 * 
	 * @param slot
	 *            slot that changed.
	 */
	private void onItemChanged(int slot, Item newItem) {
		if (newItem == null) {
			itemSlots[slot].setName("EMPTY");
			itemSlots[slot].setItemImage(null);
		} else {
			String newName = newItem.getName();
			itemSlots[slot].setItemImage(ImageList.getImageFor(newItem));

			itemSlots[slot].setItem(newItem);
			itemSlots[slot].setName(newName);
		}
	}

	/**
	 * A single gui item that is clickable.
	 * 
	 * @author illonis
	 * 
	 */
	private class GuiItem {
		private int x, y, slotId;
		private String name;
		private Item item;
		private BufferedImage itemImage;

		GuiItem(int slotId) {

			this.x = OUTER_GAP[3] + (BLOCKSIZE + ITEM_GAP)
					* (slotId % (Inventory.MAX_CAPACITY / 2));
			this.y = OUTER_GAP[0] + (BLOCKSIZE + ITEM_GAP)
					* (slotId / (Inventory.MAX_CAPACITY / 2));
			this.slotId = slotId;
			setName("?");
		}

		public void setItem(Item newItem) {
			this.item = newItem;
		}

		public boolean hasImage() {
			return itemImage != null;
		}

		BufferedImage getItemImage() {
			return itemImage;
		}

		void setItemImage(BufferedImage image) {
			this.itemImage = image;
		}

		long getCooldown() {
			if (item instanceof Usable)
				return ((Usable) item).getCooldown();
			return 0;
		}

		double getCooldownArc() {
			double e = 0;
			if (item instanceof Usable) {
				Usable u = (Usable) item;
				e = (double) u.getCooldown() / u.getCooldownTime() * 360;
			}
			return e;
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

		void setName(String name) {
			this.name = name;
		}

		protected Rectangle getClickableRect() {
			return new Rectangle(x + screenX, y + screenY, BLOCKSIZE, BLOCKSIZE);
		}
	}

	@Override
	public void onPlayerInformationReceived() {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			// onItemChanged(i);
		}
	}

	@Override
	public void onMouseOver(Point p) {

		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			if (itemSlots[i].getClickableRect().contains(p)) {
				try {
					getTooltipHandler().showItemTooltip(
							p,
							getInfo().getPlayer().getInventory()
									.getItemBySlot(i));
					return;
				} catch (ItemSlotIsEmptyException e) {

				} catch (ObjectNotFoundException e) {

				}
			}
		}
		getTooltipHandler().hideTooltip();
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public Rectangle getTriggerArea() {
		return new Rectangle(screenX, screenY, WIDTH, HEIGHT);
	}
}
