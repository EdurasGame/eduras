package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.UserInterface;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Displays text notifications to the user.
 * 
 * @author illonis
 * 
 */
public class NotificationPanel extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(NotificationPanel.class
			.getName());

	private final static Font NOTIFICATION_FONT = DEFAULT_FONT.deriveFont(16f)
			.deriveFont(Font.BOLD);
	private final static int Y_INSET = 80;

	private final ConcurrentLinkedQueue<String> notifications;
	private String currentMessage;
	private int y = 0;
	private int x = 0;
	private int screenWidth = 0;
	private FontMetrics metrics;
	private long lastChanged = System.currentTimeMillis();
	private static long DISPLAY_TIME = 3000;

	/**
	 * @param gui
	 *            the gui.
	 */
	public NotificationPanel(UserInterface gui) {
		super(gui);
		currentMessage = "";
		notifications = new ConcurrentLinkedQueue<String>();
	}

	@Override
	public void render(Graphics2D g2d) {
		if (metrics == null)
			metrics = g2d.getFontMetrics(NOTIFICATION_FONT);
		if (currentMessage.isEmpty()) {
			nextNotification();
			return;
		}
		long now = System.currentTimeMillis();
		if (lastChanged + DISPLAY_TIME < now) {
			nextNotification();
		}
		g2d.setColor(Color.WHITE);
		g2d.setFont(NOTIFICATION_FONT);
		g2d.drawString(currentMessage, x, y);
	}

	/**
	 * Adds a notification that is displayed after all previous messages have
	 * been displayed.
	 * 
	 * @param message
	 *            the message.
	 */
	public void addNotification(String message) {
		notifications.add(message);
	}

	private void nextNotification() {
		String next = notifications.poll();
		if (next == null)
			currentMessage = "";
		else {
			currentMessage = next;
			int stringWidth = metrics.stringWidth(next);
			x = (screenWidth - stringWidth) / 2;
			lastChanged = System.currentTimeMillis();
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		y = Y_INSET;
		screenWidth = newWidth;
	}

	@Override
	public void onPlayerInformationReceived() {
	}

	@Override
	public void onDeath(DeathEvent event) {
		try {
			PlayerMainFigure killer = getInfo().getPlayerByOwnerId(
					event.getKillerOwner());
			int killedOwner = getInfo().findObjectById(event.getKilled())
					.getOwner();
			PlayerMainFigure killed = getInfo().getPlayerByOwnerId(killedOwner);
			String note;
			if (getInfo().getPlayer().equals(killed)) {
				note = Localization.getStringF(
						"Client.gui.notifications.killedyou", killer.getName());
			} else
				note = Localization.getStringF("Client.gui.notifications.kill",
						killer.getName(), killed.getName());
			addNotification(note);

		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "object not found", e);
		}
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		String name = event.getName();
		String note;
		try {
			if (name.equals(getInfo().getPlayer().getName()))
				note = Localization.getStringF(
						"Client.gui.notifications.welcome", getInfo()
								.getMapName());
			else
				note = Localization.getStringF("Client.gui.notifications.join",
						name);
			addNotification(note);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "object not found", e);
		}

	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		if (event.getOwner() != getInfo().getOwnerID())
			return;
		int slot = event.getItemSlot();
		try {
			Item item = getInfo().getPlayer().getInventory()
					.getItemBySlot(slot);
			String note = Localization.getStringF(
					"Client.gui.notifications.loot", item.getName());
			addNotification(note);
		} catch (ItemSlotIsEmptyException i) {
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "object not found", e);
		}
		super.onItemSlotChanged(event);
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		if (setModeEvent.getOwner() != getInfo().getOwnerID())
			return;
		addNotification("entered " + setModeEvent.getNewMode());
	}
}
