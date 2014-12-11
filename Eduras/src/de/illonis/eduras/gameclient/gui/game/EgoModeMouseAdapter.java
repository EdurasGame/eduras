package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.InteractMode;

/**
 * Handles mouse events in the ego mode ({@link InteractMode#MODE_EGO}).
 * 
 * @author illonis
 * 
 */
public class EgoModeMouseAdapter extends GuiMouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(EgoModeMouseAdapter.class.getName());

	boolean fireButtonHold = false;

	protected EgoModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);

		logic.registerGameEventListener(new GameEventAdapter() {
			@Override
			public void onCooldownFinished(
					de.illonis.eduras.events.ItemEvent event) {
				if (fireButtonHold) {
					ObjectType currentItemSelected = getCurrentSelectedItemType();
					if (currentItemSelected != ObjectType.NO_OBJECT
							&& EdurasInitializer.getInstance().getSettings()
									.getBooleanSetting("continuousItemUsage")) {
						itemUsed(currentItemSelected, getPanelLogic()
								.getCurrentMousePos(), true);
					}
				}
			}
		});
	}

	private void egoModeClick(int button, int x, int y, int clickCount) {
		ObjectType currentItemSelected = getCurrentSelectedItemType();

		// which mouse button?
		switch (button) {
		case Input.MOUSE_LEFT_BUTTON:
			// if an item is selected...
			if (currentItemSelected != ObjectType.NO_OBJECT) {

				// use it instantly
				itemUsed(currentItemSelected, new Vector2df(x, y), false);
			}
			fireButtonHold = true;
			break;

		default:
			L.fine("Button " + button + " not assigned to anything.");
		}

	}

	/**
	 * Sends an item use event to server.
	 * 
	 * @param type
	 *            item type.
	 * @param target
	 *            target position
	 * @param isGameCoordinate
	 *            tells if the given target is already a gamecoordinate
	 */

	void itemUsed(ObjectType type, Vector2f target, boolean isGameCoordinate) {
		if (!isGameCoordinate) {
			getListener().onItemUse(type,
					getPanelLogic().computeGuiPointToGameCoordinate(target));
		} else {
			getListener().onItemUse(type, target);
		}
	}

	@Override
	public void itemClicked(int i) {
		if (i >= 0 && i < Inventory.MAX_CAPACITY) {
			try {
				getPanelLogic().selectItem(i);
			} catch (ItemSlotIsEmptyException e) {
				SoundMachine.play(SoundType.ERROR);
			}
		} else {
			getPanelLogic().setClickState(ClickState.DEFAULT);
			getPanelLogic().getClientData().setCurrentItemSelected(
					ObjectType.NO_OBJECT);
		}
	}

	private void egoModeMove(int oldx, int oldy, int newx, int newy) {
		getListener().onViewingDirectionChanged(
				getPanelLogic().computeGuiPointToGameCoordinate(
						new Vector2f(newx, newy)));
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// egoModeClick(button, x, y, clickCount);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		egoModeMove(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		egoModeMove(oldx, oldy, newx, newy);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		egoModeClick(button, x, y, 1);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {

		switch (button) {
		case Input.MOUSE_LEFT_BUTTON: {
			fireButtonHold = false;
			break;
		}
		default:
			L.fine("Released not assigned button " + button);
		}

	}

	@Override
	public void mouseWheelMoved(int change) {
		if (EdurasInitializer.getInstance().getSettings()
				.getBooleanSetting(Settings.MOUSE_WHEEL_SWITCH)) {
			if (change < 0) {
				getPanelLogic().selectNextItem();
			} else {
				getPanelLogic().selectPreviousItem();
			}
		} else {
			// do nothing
		}
	}

	private ObjectType getCurrentSelectedItemType() {
		return getPanelLogic().getClientData().getCurrentItemSelected();
	}

	@Override
	public void mapClicked(Vector2f gamePos) {
	}

	@Override
	public void mouseLost() {
		fireButtonHold = false;
	}

}
