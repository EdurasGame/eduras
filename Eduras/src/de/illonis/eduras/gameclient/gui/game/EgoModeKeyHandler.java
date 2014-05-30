package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.units.InteractMode;

/**
 * A keyhandler for {@link InteractMode#MODE_EGO}.
 * 
 * @author illonis
 * 
 */
public class EgoModeKeyHandler extends GuiKeyHandler {

	EgoModeKeyHandler(UserInputListener client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) throws NotWithinBaseException {
		switch (key) {
		case MOVE_UP:
			reactor.onStartMovement(Direction.TOP);
			break;
		case MOVE_LEFT:
			reactor.onStartMovement(Direction.LEFT);
			break;
		case MOVE_DOWN:
			reactor.onStartMovement(Direction.BOTTOM);
			break;
		case MOVE_RIGHT:
			reactor.onStartMovement(Direction.RIGHT);
			break;
		case ITEM_1:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(0);
			} else {
				reactor.onItemUse(0, client.getCurrentMousePos());
			}
			break;
		case ITEM_2:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(1);
			} else {
				reactor.onItemUse(1, client.getCurrentMousePos());
			}
			break;
		case ITEM_3:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(2);
			} else {
				reactor.onItemUse(2, client.getCurrentMousePos());
			}
			break;
		case ITEM_4:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(3);
			} else {
				reactor.onItemUse(3, client.getCurrentMousePos());
			}
			break;
		case ITEM_5:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(4);
			} else {
				reactor.onItemUse(4, client.getCurrentMousePos());
			}
			break;
		case ITEM_6:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(5);
			} else {
				reactor.onItemUse(5, client.getCurrentMousePos());
			}
			break;
		case SHOW_STATS:
			client.showStatWindow();
			break;
		case SWITCH_MODE:
			reactor.onModeSwitch();
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
		case MOVE_UP:
			reactor.onStopMovement(Direction.TOP);
			break;
		case MOVE_LEFT:
			reactor.onStopMovement(Direction.LEFT);
			break;
		case MOVE_DOWN:
			reactor.onStopMovement(Direction.BOTTOM);
			break;
		case MOVE_RIGHT:
			reactor.onStopMovement(Direction.RIGHT);
			break;
		case SHOW_STATS:
			client.hideStatWindow();
			break;
		default:
			break;
		}
	}

	@Override
	boolean isChatEnabled() {
		return true;
	}
}
