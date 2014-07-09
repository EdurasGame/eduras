package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;

/**
 * {@link GuiKeyHandler} for a dead player.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class DeadModeKeyHandler extends AnyModeKeyHandler {

	private final static Logger L = EduLog
			.getLoggerFor(DeadModeKeyHandler.class.getName());

	DeadModeKeyHandler(GamePanelLogic client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) throws ActionFailedException {
		switch (key) {
		case SHOW_STATS:
		case CHAT:
		case EXIT_CLIENT:
			super.keyPressed(key);
			break;
		default:
			throw new ActionFailedException("You cannot do this. You are dead!");
		}
	}

	@Override
	void keyReleased(KeyBinding key) {
		super.keyReleased(key);
	}
}
