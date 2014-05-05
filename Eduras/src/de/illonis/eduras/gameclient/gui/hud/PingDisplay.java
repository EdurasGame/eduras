package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Displays current latency on top right of screen.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PingDisplay extends RenderedGuiObject implements PingListener {

	private final static int WIDTH = 80;
	private final static int HEIGHT = 20;
	private String latency;

	protected PingDisplay(UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 60;
		latency = "Latency: Unknown";
	}

	@Override
	public void render(Graphics g2d) {
		g2d.setColor(Color.white);
		g2d.drawString(latency, screenX + 6, screenY + HEIGHT - 5);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - WIDTH;
	}

	@Override
	public void setPING(long value) {
		this.latency = "Latency: " + value;
	}

}
