package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;

public class BlinkDisplay extends CooldownGuiObject {

	private final static Logger L = EduLog.getLoggerFor(BlinkDisplay.class
			.getName());

	public BlinkDisplay(UserInterface gui) {
		super(gui);

		setActiveInteractModes(InteractMode.MODE_EGO);
	}

	@Override
	public void render(Graphics g) {
		Font font = FontCache.getFont(FontKey.SMALL_FONT, g);
		try {
			Image blinkIcon;
			try {
				blinkIcon = ImageCache.getGuiImage(ImageKey.SKILL_BLINK);
			} catch (CacheException e) {
				L.log(Level.SEVERE, "Cannot find blink image!", e);
				return;
			}

			g.drawImage(blinkIcon, screenX, screenY);

			g.setColor(Color.white);
			font.drawString(screenX + (3f / 7f) * blinkIcon.getWidth(), screenY
					+ (2f / 7f) * blinkIcon.getHeight(), getInfo().getPlayer()
					.getBlinksAvailable() + "");

			renderCooldown(g, screenX, screenY, blinkIcon.getWidth(),
					blinkIcon.getHeight());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find player!", e);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = newHeight / 2;
	}

	@Override
	long getCooldown() {
		try {
			return Math.max(0, getInfo().getPlayer().getBlinkCooldown());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find player!", e);
			return 0;
		}
	}

	@Override
	long getCooldownTime() {
		return S.Server.sv_blink_cooldown;
	}
}
