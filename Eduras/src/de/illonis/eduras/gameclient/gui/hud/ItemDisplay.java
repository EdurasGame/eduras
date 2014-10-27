package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.units.InteractMode;

/**
 * Displays player items on user interface.
 * 
 * @author illonis
 * 
 */
public class ItemDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(ItemDisplay.class
			.getName());

	private static final String EMPTY_NAME = "EMPTY";
	/**
	 * Alpha value for inactive items. 0 means invisible, 1 means opaque. This
	 * will affect all rendered colors on items.
	 */
	private final static float ITEM_ALPHA = 0.5f;
	private final static Color COLOR_MULTIPLIER = new Color(1f, 1f, 1f,
			ITEM_ALPHA);
	private final static Color FONT_BACKGROUND = new Color(0f, 0f, 0f, .7f);
	private final static int ITEM_GAP = 5;
	public final static int BLOCKSIZE = 36;
	private final static int FONT_PADDING = 2;

	private final float buttonSize;
	private final MiniMap minimap;
	private final GuiItem itemSlots[];
	private final float borderSize;
	private final KeyBindings bindings;

	private ObjectType currentItem = ObjectType.NO_OBJECT;

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
		this.minimap = map;
		bindings = EdurasInitializer.getInstance().getSettings()
				.getKeyBindings();
		buttonSize = BLOCKSIZE * GameRenderer.getRenderScale();
		borderSize = 2 * GameRenderer.getRenderScale();
		screenX = 15;
		itemSlots = new GuiItem[Inventory.MAX_CAPACITY];
		setActiveInteractModes(InteractMode.MODE_EGO);
		for (int i = 0; i < Inventory.MAX_CAPACITY; i++) {
			itemSlots[i] = new GuiItem(gui, i);
		}
	}

	@Override
	public void render(Graphics g) {
		currentItem = getInfo().getClientData().getCurrentItemSelected();
		Item[] items;
		try {
			items = getInfo().getPlayer().getInventory().getAllItems();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Cannot find player!", e1);
			return;
		}
		for (int i = 0; i < items.length; i++) {
			onItemChanged(i, items[i]);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		if (event.getOwner() == getInfo().getOwnerID()) {
			Item item;
			try {
				item = (Item) getInfo().findObjectById(event.getObjectId());
				onItemChanged(event.getItemSlot(), item);
			} catch (ObjectNotFoundException e) {
				onItemChanged(event.getItemSlot(), null);
			}

		}
		recalculatePosition();
	}

	private void recalculatePosition() {
		try {
			int items = getInfo().getPlayer().getInventory().getNumItems();
			float height = (buttonSize + ITEM_GAP) * items - ITEM_GAP;
			screenY = (Display.getHeight() - height - minimap.getSize()) / 2;
			for (int i = 0; i < itemSlots.length; i++) {
				itemSlots[i].updateClickRect();
			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Player not found in itemdisplay.", e);
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
		private int slotId;
		private String name;
		private Item item;
		private Image itemImage;
		private Rectangle clickRect;

		GuiItem(UserInterface gui, int slotId) {
			super(gui);
			this.slotId = slotId;
			setName(EMPTY_NAME);
			setActiveInteractModes(ItemDisplay.this.getActiveInteractModes()
					.toArray(new InteractMode[] {}));
			updateClickRect();
		}

		void updateClickRect() {
			clickRect = new Rectangle(getScreenX(), getScreenY()
					+ Math.round((buttonSize + ITEM_GAP) * slotId), buttonSize,
					buttonSize);
		}

		public void setItem(Item newItem) {
			this.item = newItem;
		}

		public boolean hasImage() {
			return itemImage != null;
		}

		void setItemImage(Image image) {
			this.itemImage = image;
		}

		public boolean isWeapon() {
			return (item instanceof Weapon);
		}

		public boolean isEmpty() {
			return name.equals(EMPTY_NAME) && itemImage == null;
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

		void setName(String name) {
			this.name = name;
		}

		@Override
		public void render(Graphics g) {
			if (isEmpty()) {
				return;
			}
			g.setLineWidth(1f);
			Font font = FontCache.getFont(FontKey.TOOLTIP_FONT, g);
			Color currentColor = (item.getType() == currentItem) ? Color.white
					: COLOR_MULTIPLIER;

			g.setColor(currentColor);

			if (hasImage()) {
				g.drawImage(itemImage, clickRect.getX(), clickRect.getY(),
						currentColor);
			}

			if (item.getType() == currentItem) {
				g.setColor(Color.yellow.multiply(COLOR_MULTIPLIER));
			} else {
				g.setColor(COLOR_MULTIPLIER);
			}
			if (isWeapon()) {
				int ammo = getWeaponAmmu();
				if (ammo != -1) {
					String ammoString = ammo + "";
					float stringX = clickRect.getX() + clickRect.getWidth()
							- font.getWidth(ammoString) - borderSize;
					float stringY = clickRect.getY() + clickRect.getHeight()
							- font.getLineHeight();
					font.drawString(stringX, stringY, ammoString, currentColor);
				}
			}

			String binding = bindings.getBindingString(bindings
					.getBindingByWeapon(item.getType()));
			float bindingWidth = font.getWidth(binding);
			font.drawString(clickRect.getX() - bindingWidth - 2,
					clickRect.getY() + (buttonSize - font.getLineHeight()) / 2,
					binding, COLOR_MULTIPLIER);

			renderCooldown(g, clickRect.getX(), clickRect.getY(),
					clickRect.getWidth(), clickRect.getHeight());
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
	public void onGameReady() {
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
				itemInSlot = playerInventory.getItemAt(i);
				onItemChanged(i, itemInSlot);
			} catch (ItemSlotIsEmptyException e) {
				// that's okay
			}
		}
		recalculatePosition();
	}
}
