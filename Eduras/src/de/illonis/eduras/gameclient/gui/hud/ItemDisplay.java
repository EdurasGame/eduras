package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.units.InteractMode;

/**
 * Displays player items on user interface.
 * 
 * @author illonis
 * 
 */
public class ItemDisplay extends ClickableGuiElement implements
		TooltipTriggerer {

	private final static Logger L = EduLog.getLoggerFor(ItemDisplay.class
			.getName());

	private static final String EMPTY_NAME = "EMPTY";

	private final static int ITEM_GAP = 10;
	private final static int BLOCKSIZE = 48;
	private int currentItem = -1;
	private final static Color COLOR_SEMITRANSPARENT = new Color(0, 0, 0, 120);
	private final float buttonSize;
	private Rectangle bounds;

	// top, right, bottom, left
	private final static int OUTER_GAP[] = { 10, 5, 10, 10 };

	/**
	 * Width of the total inventory.
	 */
	public final static int WIDTH = BLOCKSIZE * 3 + 4 * ITEM_GAP;

	private GuiItem itemSlots[];
	private final float rectStroke = 3f;

	/**
	 * Creates a new item toolbar.
	 * 
	 * @param gui
	 *            associated gui.
	 * @param map
	 *            minimap to calculate left inset.
	 */
	public ItemDisplay(UserInterface gui, MiniMap map) {
		super(gui);
		buttonSize = BLOCKSIZE * GameRenderer.getRenderScale();
		screenX = map.getSize() + 5;
		itemSlots = new GuiItem[Inventory.MAX_CAPACITY];
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			itemSlots[i] = new GuiItem(gui, i);
		}
		registerAsTooltipTriggerer(this);
		bounds = new Rectangle(screenX, screenY, buttonSize
				* Inventory.MAX_CAPACITY / 2 + OUTER_GAP[1] + OUTER_GAP[3]
				+ ITEM_GAP * (Inventory.MAX_CAPACITY / 2 - 1), buttonSize * 2);
		setActiveInteractModes(InteractMode.MODE_EGO);
	}

	@Override
	public void render(Graphics g) {
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		currentItem = getInfo().getClientData().getCurrentItemSelected();
		g.setLineWidth(rectStroke);
		for (GuiItem item : itemSlots) {

			if (item.isEmpty()) {
				continue;
			}

			String idString = "#" + (item.getSlotId() + 1);
			font.drawString(item.getX() + screenX + 1, item.getY() + screenY
					- font.getLineHeight(), idString);

			if (item.isWeapon()) {
				int ammo = item.getWeaponAmmu();
				String ammoString = "*" + ammo;
				font.drawString(
						item.getX() + screenX + buttonSize
								- font.getWidth(ammoString) - 3, item.getY()
								+ screenY - font.getLineHeight(), ammoString);
			}
			g.setColor(Color.white);
			Rectangle itemRect = item.getClickableRect();
			if (item.hasImage())
				g.drawImage(item.getItemImage(), itemRect.getX(),
						itemRect.getY());
			item.renderCooldown(g, itemRect.getX(), itemRect.getY(),
					itemRect.getWidth(), itemRect.getHeight());
			if (item.getSlotId() == currentItem) {
				g.setColor(Color.yellow);
			} else {
				g.setColor(Color.white);
			}
			g.draw(itemRect);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
	}

	@Override
	public boolean mousePressed(int button, int x, int y) {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			if (itemSlots[i].isEmpty()
					&& itemSlots[i].getClickableRect().contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			if (!itemSlots[i].isEmpty()
					&& itemSlots[i].getClickableRect().contains(x, y)) {
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
	public void itemClicked(int itemSlot) {

		try {
			getInfo().getPlayer().getInventory().getItemBySlot(itemSlot);
			getMouseHandler().itemClicked(itemSlot);
			currentItem = itemSlot;

		} catch (ItemSlotIsEmptyException e) {
			L.fine("Player tried to use empty item slot " + itemSlot + ".");
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Object not found", e);
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
			itemSlots[slot].setName(EMPTY_NAME);
			itemSlots[slot].setItemImage(null);
		} else {
			String newName = newItem.getName();
			try {
				Image image = ImageCache.getInventoryIcon(newItem.getType());
				itemSlots[slot].setItemImage(image);
			} catch (CacheException e) {
				itemSlots[slot].setItemImage(null);
				L.log(Level.SEVERE,
						"Missing cache image for: " + newItem.getType(), e);
			}

			itemSlots[slot].setName(newName);
		}
		itemSlots[slot].setItem(newItem);
	}

	/**
	 * A single gui item that is clickable.
	 * 
	 * @author illonis
	 * 
	 */
	private class GuiItem extends CooldownGuiObject {
		private int x, y, slotId;
		private String name;
		private Item item;
		private Image itemImage;
		private Rectangle clickRect;

		GuiItem(UserInterface gui, int slotId) {
			super(gui);

			this.slotId = slotId;
			setName(EMPTY_NAME);
			updateClickRect(0);
		}

		void updateClickRect(int lineHeight) {
			this.x = OUTER_GAP[3] + ((int) buttonSize + ITEM_GAP)
					* (slotId % (Inventory.MAX_CAPACITY / 2));
			this.y = lineHeight + OUTER_GAP[0]
					+ ((int) buttonSize + lineHeight + ITEM_GAP)
					* (slotId / (Inventory.MAX_CAPACITY / 2));
			clickRect = new Rectangle(x + getScreenX(), y + getScreenY(),
					buttonSize, buttonSize);
		}

		public void setItem(Item newItem) {
			this.item = newItem;
		}

		public boolean hasImage() {
			return itemImage != null;
		}

		Image getItemImage() {
			return itemImage;
		}

		void setItemImage(Image image) {
			this.itemImage = image;
		}

		public boolean isWeapon() {
			return (item instanceof Weapon);
		}

		public boolean isEmpty() {
			return name.equals(EMPTY_NAME) && getItemImage() == null;
		}

		public int getWeaponAmmu() {
			if (isWeapon()) {
				return ((Weapon) item).getCurrentAmmunition();
			}
			return 0;
		}

		@Override
		long getCooldown() {
			if (item instanceof Usable)
				return ((Usable) item).getCooldown();
			return 0;
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
			return clickRect;
		}

		@Override
		public void render(Graphics g) {
		}

		@Override
		public void onGuiSizeChanged(int newWidth, int newHeight) {
		}

		@Override
		long getCooldownTime() {
			if (item instanceof Usable)
				return ((Usable) item).getCooldownTime();
			return 0;
		}
	}

	float getScreenX() {
		return screenX;
	}

	float getScreenY() {
		return screenY;
	}

	@Override
	public void onMouseOver(Vector2f p) {
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			if (!itemSlots[i].isEmpty()
					&& itemSlots[i].getClickableRect().contains(p.x, p.y)) {
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
		return getTriggerArea();
	}

	@Override
	public Rectangle getTriggerArea() {
		return bounds;
	}

	@Override
	public void onGameReady() {
		try {
			int lineHeight = FontCache.getFont(FontKey.DEFAULT_FONT)
					.getLineHeight();
			bounds.setHeight(buttonSize * 2 + lineHeight * 2 + OUTER_GAP[0]
					+ OUTER_GAP[2] + ITEM_GAP);
			bounds.setWidth(buttonSize * Inventory.MAX_CAPACITY / 2
					+ OUTER_GAP[1] + OUTER_GAP[3] + ITEM_GAP
					* (Inventory.MAX_CAPACITY / 2 - 1));
			screenY = Display.getHeight() - bounds.getHeight();
			bounds.setY(screenY);
			for (int i = 0; i < itemSlots.length; i++) {
				itemSlots[i].updateClickRect(lineHeight);
			}
		} catch (CacheException e) {
			System.out.println("no font");
		}
		Inventory playerInventory;
		try {
			playerInventory = getInfo().getPlayer().getInventory();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Cannot find player!", e1);
			return;
		}
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			Item itemInSlot;
			try {
				itemInSlot = playerInventory.getItemBySlot(i);
				onItemChanged(i, itemInSlot);
			} catch (ItemSlotIsEmptyException e) {
				// that's okay
			}
		}
	}
}
