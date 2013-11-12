package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.UserInterface;
import de.illonis.eduras.gamemodes.GameMode;

/**
 * A simple status bar that shows the current game mode at top of screen.
 * 
 * @author illonis
 * 
 */
public class GameModeBar extends RenderedGuiObject {
	private String mode;

	/**
	 * Creates the game stat bar.
	 * 
	 * @param gui
	 *            associated gui.
	 */
	public GameModeBar(UserInterface gui) {
		super(gui);
		setVisibleForSpectator(true);
		mode = "unknown game mode";
		screenX = 30;
		screenY = 0;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.GRAY);
		g2d.fillRect(screenX, screenY, 200, 30);
		g2d.setColor(Color.BLACK);
		g2d.drawString(mode, screenX + 20, screenY + 20);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth / 2 - 100;
	}

	@Override
	public void onPlayerInformationReceived() {
	}

	@Override
	public void onGameModeChanged(GameMode newMode) {
		mode = newMode.getName();
	}

	@Override
	public void onGameReady() {
		// TODO Auto-generated method stub

	}

}
