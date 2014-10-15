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
public class BuildModeKeyHandler extends AnyModeKeyHandler {

	BuildModeKeyHandler(UserInputListener client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) {
		super.keyPressed(key);

		switch (key) {
		case SWITCH_MODE:
			try {
				reactor.onModeSwitch();
				client.resetCamera();
			} catch (NotWithinBaseException e) {
				client.onActionFailed(e);
			}
			break;
		case STRATEGY_1:
			client.selectActionButton(0);
			break;
		case STRATEGY_2:
			client.selectActionButton(1);
			break;
		case STRATEGY_3:
			client.selectActionButton(2);
			break;
		case STRATEGY_4:
			client.selectActionButton(3);
			break;
		case STRATEGY_5:
			client.selectActionButton(4);
			break;
		case STRATEGY_6:
			client.selectActionButton(5);
			break;
		case STRATEGY_7:
			client.selectActionButton(6);
			break;
		case STRATEGY_8:
			client.selectActionButton(7);
			break;
		case STRATEGY_9:
			client.selectActionButton(8);
			break;
		case STRATEGY_10:
			client.selectActionButton(9);
			break;
		case PAGE_UP:
			client.pageUp();
		default:
			break;
		}
	}

	@Override
	void keyReleased(KeyBinding key) {
		super.keyReleased(key);
	}

	@Override
	boolean isChatEnabled() {
		return true;
	}
}
