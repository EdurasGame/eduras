package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.gameclient.ChatCache;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;

/**
 * Displays a chat.
 * 
 * @author illonis
 * 
 */
public class ChatDisplay extends RenderedGuiObject {

	private final static int MAX_LINES = 5;
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
		Font font = FontCache.getFont(FontKey.CHAT_FONT, g);
		g.setColor(Color.white);

		ChatMessage msg;
		int i = font.getLineHeight();
		if (data.isWriting())
			i += font.getLineHeight();

		int height = (MAX_LINES + 1) * font.getLineHeight();
		font.drawString(screenX + WIDTH - 130, screenY - height, "Room: "
				+ data.getRoomName());
		while (i < height && null != (msg = data.popMessage())) {
			if (msg.isSystemMessage())
				g.setColor(Color.yellow);
			else
				g.setColor(Color.white);
			font.drawString(screenX + 5, screenY - i, msg.toChatWindowString());
			i += font.getLineHeight();
		}
		if (data.isWriting()) {
			g.setColor(Color.white);
			font.drawString(screenX + 5, screenY - font.getLineHeight(),
					data.getInput() + "_");
		}
		data.resetPop();
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight;
		screenX = newWidth * 2 / 3;
	}
}
