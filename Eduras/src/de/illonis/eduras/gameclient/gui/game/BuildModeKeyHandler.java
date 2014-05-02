package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.units.InteractMode;

/**
 * A keyhandler for {@link InteractMode#MODE_STRATEGY}.
 * 
 * @author illonis
 * 
 */
public class BuildModeKeyHandler extends GuiKeyHandler {

	BuildModeKeyHandler(GamePanelLogic client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) throws NotWithinBaseException {
		switch (key) {
		case SWITCH_MODE:
			client.getCamera().reset();
			reactor.onModeSwitch();
			break;
		default:
			break;
		}
	}

	@Override
	void keyReleased(KeyBinding key) {
	}
}
