package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

/**
 * A keyhandler for specator.
 * 
 * @author illonis
 * 
 */
public class SpectatorKeyHandler extends AnyModeKeyHandler {

	SpectatorKeyHandler(UserInputListener client, GamePanelReactor reactor) {
		super(client, reactor);
	}

	@Override
	void keyPressed(KeyBinding key) {
		super.keyPressed(key);

		switch (key) {
		case CANCEL:
			client.cancel();
			break;
		case SPECTATOR_SCROLL_DOWN:
			client.startCameraMovement(Direction.BOTTOM, 1);
			break;
		case SPECTATOR_SCROLL_LEFT:
			client.startCameraMovement(Direction.LEFT, 1);
			break;
		case SPECTATOR_SCROLL_RIGHT:
			client.startCameraMovement(Direction.RIGHT, 1);
			break;
		case SPECTATOR_SCROLL_UP:
			client.startCameraMovement(Direction.TOP, 1);
			break;
		default:
			break;
		}
	}

	@Override
	void keyReleased(KeyBinding key) {
		super.keyReleased(key);
		switch (key) {
		case SCROLL_DOWN:
			client.stopCameraMovement(Direction.BOTTOM, 1);
			break;
		case SCROLL_LEFT:
			client.stopCameraMovement(Direction.LEFT, 1);
			break;
		case SCROLL_RIGHT:
			client.stopCameraMovement(Direction.RIGHT, 1);
			break;
		case SCROLL_UP:
			client.stopCameraMovement(Direction.TOP, 1);
			break;
		default:
			break;
		}
	}

	@Override
	boolean isChatEnabled() {
		return false;
	}
}
