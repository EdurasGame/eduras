package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;

/**
 * This is a key handler that handles key bindings which should apply the same
 * in any mode like the ability to chat, view the stats and exit the client.
 * 
 * @author Florian Mai
 * 
 */
public abstract class AnyModeKeyHandler extends GuiKeyHandler {

	private final static Logger L = EduLog.getLoggerFor(AnyModeKeyHandler.class
			.getName());

	protected AnyModeKeyHandler(GamePanelLogic client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) throws ActionFailedException {
		switch (key) {
		case SHOW_STATS:
			client.showStatWindow();
			break;
		case CHAT:
			client.onChatEnter();
			break;
		case EXIT_CLIENT:
			if (!client.abortChat())
				reactor.onGameQuit();
			break;
		default:
			break;
		}
	}

	@Override
	void keyReleased(KeyBinding key) {
		switch (key) {
		case SHOW_STATS:
			client.hideStatWindow();
			break;
		default:
			break;
		}
	}
}
