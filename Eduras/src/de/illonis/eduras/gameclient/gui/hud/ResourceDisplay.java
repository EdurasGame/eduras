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
import de.illonis.eduras.units.InteractMode;

/**
 * Displays team resources.
 * 
 * @author illonis
 * 
 */
public class ResourceDisplay extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(ResourceDisplay.class
			.getName());

	private Player player;
	private Image icon;
	private int resAmount;
	private Font font;
	private float textX;
	private float iconY;

	protected ResourceDisplay(UserInterface gui) {
		super(gui);
		resAmount = 0;

		setActiveInteractModes(InteractMode.MODE_STRATEGY);
	}

	@Override
	public void render(Graphics g) {

		g.drawImage(icon, screenX, iconY);
		font.drawString(textX, screenY, resAmount + "", Color.white);
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		screenX = ((float) windowWidth * 3) / 4;
		screenY = 10;
		font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
		try {
			icon = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON);
		} catch (CacheException e) {
			L.log(Level.SEVERE, "Resource icon not found.", e);
			return false;
		}
		iconY = screenY + (font.getLineHeight() - icon.getHeight()) / 2;
		textX = screenX + icon.getWidth() + 3;
		return true;
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
