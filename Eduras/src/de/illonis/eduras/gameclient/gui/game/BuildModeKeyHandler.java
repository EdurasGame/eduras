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
		case ITEM_ASSAULT:
			client.selectActionButton(0);
			break;
		case ITEM_SWORD:
			client.selectActionButton(1);
			break;
		case ITEM_SNIPER:
			client.selectActionButton(2);
			break;
		case ITEM_SPLASH:
			client.selectActionButton(3);
			break;
		case ITEM_SIMPLE:
			client.selectActionButton(4);
			break;
		case ITEM_MINE:
			client.selectActionButton(5);
			break;
		case ITEM_ROCKET:
			client.selectActionButton(6);
			break;
		case ITEM_8:
			client.selectActionButton(7);
			break;
		case ITEM_9:
			client.selectActionButton(8);
			break;
		case ITEM_10:
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
