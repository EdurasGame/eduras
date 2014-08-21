package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;

/**
 * Displays team resources.
 * 
 * @author illonis
 * 
 */
public class ResourceDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(ResourceDisplay.class
			.getName());

	private final static int ICON_WIDTH = 20;

	private Player player;
	private int resAmount;

	protected ResourceDisplay(UserInterface gui) {
		super(gui);
		resAmount = 0;
	}

	@Override
	public void render(Graphics g) {
		Image i = null;
		try {
			i = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Resource icon not found.", e);
		}
		if (i == null)
			return;
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		g.drawImage(i, screenX, screenY
				+ (font.getLineHeight() - i.getHeight()) / 2);
		g.setColor(Color.white);
		font.drawString(screenX + ICON_WIDTH + 3, screenY, resAmount + "");
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - 250;
		screenY = 10;
	}

	@Override
	public void onGameReady() {
		try {
			player = getInfo().getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE,
					"Player not found after playerinformation received.", e);
		}
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		if (player == null) {
			return;
		}

		try {
			resAmount = player.getTeam().getResource();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.WARNING,
					"Player doesn't have a team (yet). Won't draw the resources",
					e);
		}
	}
}
