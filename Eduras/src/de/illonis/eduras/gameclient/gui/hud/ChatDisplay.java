package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
	private final ChatCache data;

	protected ChatDisplay(ChatCache chatData, UserInterface gui) {
		super(gui);
		screenX = 0;
		screenY = 0;
		this.data = chatData;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawString("Room: " + data.getRoomName(), screenX + WIDTH - 130,
				screenY + 20);
		ChatMessage msg;
		int i = 10;
		if (data.isWriting())
			i = 25;

		while (i < HEIGHT - 15 && null != (msg = data.popMessage())) {
			if (msg.isSystemMessage())
				g.setColor(Color.yellow);
			else
				g.setColor(Color.white);
			g.drawString(msg.toChatWindowString(), screenX + 5, screenY
					+ HEIGHT - i);
			i += 15;
		}
		if (data.isWriting()) {
			g.setColor(Color.white);
			g.drawString(data.getInput() + "_", screenX + 5, screenY + HEIGHT
					- 10);
		}
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
