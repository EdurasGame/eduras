package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.ChatCache;

/**
 * Displays a chat.
 * 
 * @author illonis
 * 
 */
public class ChatDisplay extends RenderedGuiObject {

	private final static int HEIGHT = 150;
	private final static int WIDTH = 300;
	private final static Color BACKGROUND = Color.BLACK;
	private final ChatCache data;

	protected ChatDisplay(ChatCache chatData, UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 0;
		this.data = chatData;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(BACKGROUND);
		g2d.fillRect(screenX, screenY, WIDTH, HEIGHT);
		g2d.setColor(Color.WHITE);
		g2d.drawString("Room: " + data.getRoomName(), screenX, screenY + 20);
		g2d.drawString(data.popMessage(), screenX, screenY + 50);
		g2d.drawString(data.getInput(), screenX, screenY + 80);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - HEIGHT;
	}

	@Override
	public void onPlayerInformationReceived() {
	}
}
