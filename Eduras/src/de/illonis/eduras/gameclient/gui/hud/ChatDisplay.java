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
	private static final long MESSAGE_FADE_TIME = 15000;
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
		if (data.isEnabled()) {
			int height = (MAX_LINES + 1) * font.getLineHeight();

			// show name of chat room only when writing
			if (data.isWriting()) {
				font.drawString(screenX + WIDTH - 130, screenY - height,
						"Room: " + data.getRoomName(), Color.white);
			}

			ChatMessage msg;
			int i = font.getLineHeight();
			if (data.isWriting())
				i += font.getLineHeight();
			while (i < height && null != (msg = data.popMessage())) {
				if (!data.isWriting()
						&& System.currentTimeMillis() - msg.getTimeStamp() > MESSAGE_FADE_TIME) {
					break;
				}
				font.drawString(screenX + 5, screenY - i, msg
						.toChatWindowString(),
						(msg.isSystemMessage()) ? Color.yellow : Color.white);
				i += font.getLineHeight();
			}
			if (data.isWriting()) {
				font.drawString(screenX + 5, screenY - font.getLineHeight(),
						data.getInput() + "_", Color.white);
			}
			data.resetPop();
		} else {
			font.drawString(screenX + WIDTH / 2,
					screenY - font.getLineHeight(), "Chat is disabled.",
					Color.yellow);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - MiniMap.SIZE;
		screenX = newWidth * 2 / 3;
	}
}
