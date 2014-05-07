package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
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
		try {
			Image i = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON);
			g.drawImage(i, screenX, screenY);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Resource icon not found.", e);
		}
		g.setColor(Color.white);
		g.drawString(resAmount + "", screenX + ICON_WIDTH + 3, screenY + 2);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - 150;
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
		resAmount = player.getTeam().getResource();
	}
}
