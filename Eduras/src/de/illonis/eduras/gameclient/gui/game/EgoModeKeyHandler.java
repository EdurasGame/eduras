package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.gameclient.CantSpawnHereException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.InsufficientChargesException;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
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
		case ITEM_ASSAULT:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.ASSAULTRIFLE);
			} else {
				reactor.onItemUse(ObjectType.ASSAULTRIFLE,
						client.getCurrentMousePos());
			}
			break;
		case ITEM_SWORD:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.ITEM_WEAPON_SWORD);
			} else {
				reactor.onItemUse(ObjectType.ITEM_WEAPON_SWORD,
						client.getCurrentMousePos());
			}
			break;
		case ITEM_SNIPER:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.ITEM_WEAPON_SNIPER);
			} else {
				reactor.onItemUse(ObjectType.ITEM_WEAPON_SNIPER,
						client.getCurrentMousePos());
			}
			break;
		case ITEM_SPLASH:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.ITEM_WEAPON_SPLASH);
			} else {
				reactor.onItemUse(ObjectType.ITEM_WEAPON_SPLASH,
						client.getCurrentMousePos());
			}
			break;
		case ITEM_SIMPLE:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.ITEM_WEAPON_SIMPLE);
			} else {
				reactor.onItemUse(ObjectType.ITEM_WEAPON_SIMPLE,
						client.getCurrentMousePos());
			}
			break;
		case ITEM_MINE:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.MINELAUNCHER);
			} else {
				reactor.onItemUse(ObjectType.MINELAUNCHER,
						client.getCurrentMousePos());
			}
			break;
		case ITEM_ROCKET:
			if (settings.getBooleanSetting("chooseOnPress")) {
				client.selectItem(ObjectType.ROCKETLAUNCHER);
			} else {
				reactor.onItemUse(ObjectType.ROCKETLAUNCHER,
						client.getCurrentMousePos());
			}
			break;
		case SWITCH_MODE:
			try {
				reactor.onModeSwitch();
			} catch (NotWithinBaseException e) {
				client.onActionFailed(e);
			}
			break;
		case BLINK:
			try {
				reactor.onBlink(client.getCurrentMousePos());
			} catch (InsufficientChargesException e) {
				client.onActionFailed(e);
				SoundMachine.play(SoundType.ERROR);
			} catch (CantSpawnHereException e) {
				SoundMachine.play(SoundType.ERROR);
			}
			break;
		default:
			break;
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
