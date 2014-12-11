package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;

/**
 * Displays a single line of text above the action bar to provide some hints.
 * 
 * @author illonis
 * 
 */
public class TipPanel extends RenderedGuiObject {

	private final static long DEFAULT_DISPLAY_TIME = 3000;
	private String message;
	private long step, last, remaining;

	/**
	 * @param gui
	 *            the gui.
	 * @param map
	 *            minimap to calculate left offset.
	 */
	public TipPanel(UserInterface gui, MiniMap map) {
		super(gui);
		message = "";
		screenX = map.getSize() + 15;
		setVisibleForSpectator(false);
		last = System.currentTimeMillis();
	}

	@Override
	public void render(Graphics g2d) {
		if (!message.isEmpty()) {
			step = System.currentTimeMillis() - last;
			remaining -= step;
			FontCache.getFont(FontKey.SMALL_FONT, g2d).drawString(screenX,
					screenY, message, Color.white);
			if (remaining <= 0) {
				message = "";
			} else {
				last = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Sets the message that should be displayed. Each message shown for
	 * {@value #DEFAULT_DISPLAY_TIME}ms. Setting a new message immediately
	 * disposes the former one.
	 * 
	 * @param text
	 *            the message.
	 */
	public void setMessage(String text) {
		this.message = text;
		remaining = DEFAULT_DISPLAY_TIME;
		last = System.currentTimeMillis();
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		screenY = windowHeight - MiniMap.SIZE * GameRenderer.getRenderScale()
				- 20;
		return true;
	}

	@Override
	public void onDeath(DeathEvent event) {
		message = "";
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		message = "";
	}

}
