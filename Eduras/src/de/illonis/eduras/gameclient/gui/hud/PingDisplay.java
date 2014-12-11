package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;

/**
 * Displays current latency on top right of screen.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PingDisplay extends RenderedGuiObject implements PingListener {
	private String latency;

	protected PingDisplay(UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 80;
		latency = "999 ms";
	}

	@Override
	public void render(Graphics g2d) {
		Font font = FontCache.getFont(FontKey.SMALL_FONT, g2d);
		float x = screenX - font.getWidth(latency);
		font.drawString(x, screenY + font.getLineHeight(), latency, Color.white);
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		screenX = windowWidth - 5;
		return true;
	}

	@Override
	public void setPING(long value) {
		this.latency = value + " ms";
	}

}
