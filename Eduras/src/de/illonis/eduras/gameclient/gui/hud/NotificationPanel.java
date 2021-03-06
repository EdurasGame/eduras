package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.networking.ClientRole;
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

	private final static int Y_INSET = 50;
	private final static int BUFFER_SIZE = 5;
	private final static long DEFAULT_DISPLAY_TIME = 3000;
	private int y = 0;
	private int x = 0;
	private int screenWidth = 0;
	private DisplayMessage[] messageBuffer;
	private long step, last;

	private static class DisplayMessage {
		private String message;
		private long remaining;
		private float alpha;

		public DisplayMessage(String message) {
			this.message = message;
			remaining = DEFAULT_DISPLAY_TIME;
			alpha = 1f;
		}
	}

	/**
	 * @param gui
	 *            the gui.
	 */
	public NotificationPanel(UserInterface gui) {
		super(gui);
		messageBuffer = new DisplayMessage[BUFFER_SIZE];
		setVisibleForSpectator(true);
		last = System.currentTimeMillis();
	}

	@Override
	public void render(Graphics g2d) {
		checkRemaining();
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g2d);
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (messageBuffer[i] != null) {
				int stringWidth = font.getWidth(messageBuffer[i].message);
				x = (screenWidth - stringWidth) / 2;
				Color color = new Color(1f, 1f, 1f, messageBuffer[i].alpha);
				font.drawString(x, y + i * font.getLineHeight(),
						messageBuffer[i].message, color);
			}
		}

	}

	private void checkRemaining() {
		step = System.currentTimeMillis() - last;
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (messageBuffer[i] == null)
				continue;
			if (messageBuffer[i].remaining <= 0) {
				if (messageBuffer[i].alpha <= 0.1f) {
					messageBuffer[i] = null;
				} else {
					messageBuffer[i].alpha = Math.max(0f,
							messageBuffer[i].alpha - 0.05f);
				}
			} else {
				messageBuffer[i].remaining -= step;
			}
		}
		last = System.currentTimeMillis();
	}

	/**
	 * Adds a notification that is displayed after all previous messages have
	 * been displayed.
	 * 
	 * @param message
	 *            the message.
	 */
	public void addNotification(String message) {
		DisplayMessage msg = new DisplayMessage(message);
		bufferStep(0);
		messageBuffer[BUFFER_SIZE - 1] = msg;
	}

	private void bufferStep(int from) {
		for (int i = from; i < BUFFER_SIZE - 1; i++) {
			messageBuffer[i] = messageBuffer[i + 1];
		}
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		y = Y_INSET;
		screenWidth = windowWidth;
		return true;
	}

	@Override
	public void onDeath(DeathEvent event) {
		try {
			if (!(getInfo().getGameObjects().get(event.getKilled()) instanceof PlayerMainFigure)) {
				// TODO: decide: want to show something if the killed unit isn't
				// a player?
				return;
			}

			int killedOwner = getInfo().findObjectById(event.getKilled())
					.getOwner();
			Player killed = getInfo().getPlayerByOwnerId(killedOwner);

			Player killer = getInfo()
					.getPlayerByOwnerId(event.getKillerOwner());

			String note;
			if (getInfo().getClientData().getRole() == ClientRole.SPECTATOR
					|| !getInfo().getPlayer().equals(killed)) {
				note = Localization.getStringF("Client.gui.notifications.kill",
						killer.getName(), killed.getName());
			} else {
				note = Localization.getStringF(
						"Client.gui.notifications.killedyou", killer.getName());
			}
			addNotification(note);

		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE,
					"Could not find objects while reporting a kill on notification panel.",
					e);
		}
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		String name = event.getName();
		String note;
		if (event.getOwner() == getInfo().getOwnerID())
			note = Localization.getStringF("Client.gui.notifications.welcome",
					getInfo().getMapName());
		else
			note = Localization.getStringF("Client.gui.notifications.join",
					name);
		addNotification(note);
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		if (event.getOwner() != getInfo().getOwnerID() || !event.isNew())
			return;
		int slot = event.getItemSlot();
		try {
			Item item = getInfo().getPlayer().getInventory().getItemAt(slot);
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

	@Override
	public void onBaseConquered(Base base, Team conqueringTeam) {
		if (conqueringTeam != null) {
			addNotification(conqueringTeam.getName() + " conquered a base!");
		}
	}
}
