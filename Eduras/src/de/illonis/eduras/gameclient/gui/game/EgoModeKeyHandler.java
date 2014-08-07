package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.units.InteractMode;

/**
 * A keyhandler for {@link InteractMode#MODE_EGO}.
 * 
 * @author illonis
 * 
 */
public class EgoModeKeyHandler extends AnyModeKeyHandler {

	EgoModeKeyHandler(UserInputListener client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) {
		super.keyPressed(key);
		try {
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
			case ITEM_7:
				if (settings.getBooleanSetting("chooseOnPress")) {
					client.selectItem(6);
				} else {
					reactor.onItemUse(6, client.getCurrentMousePos());
				}
				break;
			case ITEM_8:
				if (settings.getBooleanSetting("chooseOnPress")) {
					client.selectItem(7);
				} else {
					reactor.onItemUse(7, client.getCurrentMousePos());
				}
				break;
			case ITEM_9:
				if (settings.getBooleanSetting("chooseOnPress")) {
					client.selectItem(8);
				} else {
					reactor.onItemUse(8, client.getCurrentMousePos());
				}
				break;
			case ITEM_10:
				if (settings.getBooleanSetting("chooseOnPress")) {
					client.selectItem(9);
				} else {
					reactor.onItemUse(9, client.getCurrentMousePos());
				}
				break;
			case SWITCH_MODE:
				try {
					reactor.onModeSwitch();
				} catch (NotWithinBaseException e) {
					client.onActionFailed(e);
				}
				break;
			default:
				break;
			}
		} catch (ItemSlotIsEmptyException e) {
			SoundMachine.play(SoundType.ERROR);
		}
	}

	@Override
	void keyReleased(KeyBinding key) {
		super.keyReleased(key);

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
		default:
			break;
		}
	}

	@Override
	boolean isChatEnabled() {
		return true;
	}
}
