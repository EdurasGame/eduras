package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

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
	void keyPressed(KeyBinding key) {
		switch (key) {
		case SWITCH_MODE:
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
