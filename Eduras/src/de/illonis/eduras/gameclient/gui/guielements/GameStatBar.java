package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.ImageList;
import de.illonis.eduras.logicabstraction.InformationProvider;

public class GameStatBar extends RenderedGuiObject {
	private String mode;

	public GameStatBar(InformationProvider info) {
		super(info);
		mode = "unknown game mode";
		screenX = 30;
		screenY = 0;
	}

	@Override
	public void render(Graphics2D g2d, ImageList imageList) {
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
		mode = getInfo().getGameMode().getName();
	}
}
