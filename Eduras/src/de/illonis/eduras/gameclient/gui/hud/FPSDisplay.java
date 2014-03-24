package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.gameclient.gui.game.FPSListener;

/**
 * Displays current rendering fps on top right of the screen.
 * 
 * @author illonis
 * 
 */
public class FPSDisplay extends RenderedGuiObject implements FPSListener {

	private final static float WIDTH = 50f;
	private final static float HEIGHT = 20f;
	private String fps;

	protected FPSDisplay(UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 30;
		fps = "";
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawString(fps, screenX + 6, screenY + HEIGHT - 5);
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
