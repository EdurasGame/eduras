package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.math.BasicMath;
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
					int currentItemSelected = getPanelLogic().getClientData()
							.getCurrentItemSelected();
					if (currentItemSelected != -1
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
		ClickState currentClickState = getPanelLogic().getClickState();
		int currentItemSelected = getCurrentSelectedItemIndex();

		// which mouse button?
		switch (button) {
		case Input.MOUSE_LEFT_BUTTON:

			// which state are we in?
			switch (currentClickState) {
			case ITEM_SELECTED:
				// if an item is selected...
				if (currentItemSelected != -1) {

					// use it instantly
					itemUsed(currentItemSelected, new Vector2df(x, y), false);
				}
				fireButtonHold = true;
				break;
			case DEFAULT:
				// TODO: Notify only elements that are really clicked.

				// inGameClick(e.getPoint());
				break;
			default:
				break;

			}
			break;

		default:
			L.fine("Button " + button + " not assigned to anything.");
		}

	}

	/**
	 * Sends an item use event to server.
	 * 
	 * @param i
	 *            item slot.
	 * @param target
	 *            target position
	 * @param isGameCoordinate
	 *            tells if the given target is already a gamecoordinate
	 */

	void itemUsed(int i, Vector2f target, boolean isGameCoordinate) {
		if (!isGameCoordinate) {
			getListener().onItemUse(i,
					getPanelLogic().computeGuiPointToGameCoordinate(target));
		} else {
			getListener().onItemUse(i, target);
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
			getPanelLogic().getClientData().setCurrentItemSelected(-1);
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

			int numItemsInInventory;
			try {
				numItemsInInventory = EdurasInitializer.getInstance()
						.getInformationProvider().getPlayer().getInventory()
						.getNumItems();
			} catch (ObjectNotFoundException e) {
				L.log(Level.SEVERE,
						"Cannot find player when moving the mouse wheel!", e);
				return;
			}
			int currentSelectedItem = getCurrentSelectedItemIndex();
			int itemToSelect;

			if (currentSelectedItem == -1) {
				itemToSelect = 0;
			} else {
				try {
					itemToSelect = BasicMath.calcModulo(currentSelectedItem
							+ change, numItemsInInventory);
				} catch (InvalidValueEnteredException e) {
					itemToSelect = 0;
				}
			}

			itemClicked(itemToSelect);

		} else {
			// do nothing
		}
	}

	private int getCurrentSelectedItemIndex() {
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
