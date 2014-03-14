package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Displays current latency on top right of screen.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PingDisplay extends RenderedGuiObject implements PingListener {

	private final static int WIDTH = 70;
	private final static int HEIGHT = 20;
	private static final Color TRANSLUCENT = new Color(0, 0, 0, 0);
	private String latency;

	protected PingDisplay(UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 60;
		latency = "Latency: Unknown";
	}

	@Override
	public void render(Graphics2D g2d) {
		// g2d.setColor(Color.GRAY);
		g2d.setColor(TRANSLUCENT);
		g2d.fillRect(screenX, screenY, WIDTH, HEIGHT);
		g2d.setFont(DEFAULT_FONT);
		g2d.setColor(Color.WHITE);
		g2d.drawString(latency, screenX + 6, screenY + HEIGHT - 5);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - WIDTH;
	}

	@Override
	public void onPlayerInformationReceived() {
	}

	@Override
	public void setPING(long value) {
		this.latency = "Latency: " + value;
	}

}
