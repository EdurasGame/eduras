package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.gameclient.ChatCache;

/**
 * Displays a chat.
 * 
 * @author illonis
 * 
 */
public class ChatDisplay extends RenderedGuiObject {

	private final static int HEIGHT = 150;
	private final static int WIDTH = 280;
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
		g2d.drawString("Room: " + data.getRoomName(), screenX + WIDTH - 130,
				screenY + 20);
		ChatMessage msg;
		int i = 30;
		int n = 0;
		while (n < 8 && null != (msg = data.popMessage())) {
			g2d.drawString(
					msg.getPostingUser().getNickName() + ": "
							+ msg.getMessage(), screenX + 5, screenY + HEIGHT
							- i);
			i += 15;
			n++;
		}
		if (data.isWriting())
			g2d.drawString(data.getInput() + "_", screenX + 5, screenY + HEIGHT
					- 10);
		data.resetPop();
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - HEIGHT;
		screenX = newWidth - WIDTH;
	}

	@Override
	public void onPlayerInformationReceived() {
	}
}
