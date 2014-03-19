package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.game.FPSListener;

/**
 * Displays current rendering fps on top right of the screen.
 * 
 * @author illonis
 * 
 */
public class FPSDisplay extends RenderedGuiObject implements FPSListener {

	private static final Color TRANSLUCENT = new Color(0, 0, 0, 0);

	private final static int WIDTH = 50;
	private final static int HEIGHT = 20;
	private String fps;

	protected FPSDisplay(UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 30;
		fps = "";
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setFont(DEFAULT_FONT);
		g2d.setColor(Color.WHITE);
		g2d.drawString(fps, screenX + 6, screenY + HEIGHT - 5);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth - WIDTH;
	}

	@Override
	public void onPlayerInformationReceived() {
	}

	@Override
	public void setFPS(int value) {
		this.fps = value + " fps";
	}

}
