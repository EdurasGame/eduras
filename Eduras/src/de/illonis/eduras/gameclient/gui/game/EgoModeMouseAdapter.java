package de.illonis.eduras.gameclient.gui.game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Usable;
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

	Timer itemUseTimer;

	protected EgoModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);

		itemUseTimer = null;
	}

	private void egoModeClick(int button, int x, int y, int clickCount) {
		ClickState currentClickState = getPanelLogic().getClickState();
		int currentItemSelected = getPanelLogic().getClientData()
				.getCurrentItemSelected();

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

					// and if the user prefers so, do it continuously until
					// mouse released.
					if (EdurasInitializer.getInstance().getSettings()
							.getBooleanSetting("continuousItemUsage")) {

						itemUseTimer = new Timer();
						ContinuousItemUser itemUser = new ContinuousItemUser(
								currentItemSelected);
						Usable itemToUse = null;
						try {
							itemToUse = (Usable) EdurasInitializer
									.getInstance().getInformationProvider()
									.getPlayer().getInventory()
									.getItemBySlot(currentItemSelected);
						} catch (ItemSlotIsEmptyException
								| ObjectNotFoundException e1) {
							L.log(Level.WARNING,
									"Cannot find item when trying to set up the ContinuousItemUser!",
									e1);
							return;
						}
						itemUseTimer.schedule(itemUser,
								itemToUse.getCooldownTime(),
								itemToUse.getCooldownTime());
					}
				}
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
			getPanelLogic().selectItem(i);

			if (i != getPanelLogic().getClientData().getCurrentItemSelected()) {
				discardItemUseTimer();
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

	private void discardItemUseTimer() {
		if (itemUseTimer != null) {
			itemUseTimer.cancel();
			itemUseTimer = null;
		}
	}

	class ContinuousItemUser extends TimerTask {

		private final int itemSlotNumber;

		public ContinuousItemUser(int itemSlotNumber) {
			this.itemSlotNumber = itemSlotNumber;
		}

		@Override
		public void run() {
			itemUsed(itemSlotNumber, getPanelLogic().getCurrentMousePos(), true);
		}
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
			if (EdurasInitializer.getInstance().getSettings()
					.getBooleanSetting("continuousItemUsage")) {
				discardItemUseTimer();
			}
			break;
		}
		default:
			L.fine("Released not assigned button " + button);
		}

	}

	@Override
	public void mouseWheelMoved(int change) {
	}

}
