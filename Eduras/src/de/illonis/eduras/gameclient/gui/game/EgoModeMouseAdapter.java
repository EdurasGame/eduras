package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * Handles mouse events in the ego mode ({@link InteractMode#MODE_EGO}).
 * 
 * @author illonis
 * 
 */
public class EgoModeMouseAdapter extends GuiMouseAdapter {

	protected EgoModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);

	}

	private void egoModeClick(int button, int x, int y, int clickCount) {
		ClickState currentClickState = getPanelLogic().getClickState();
		int currentItemSelected = getPanelLogic().getClientData()
				.getCurrentItemSelected();

		switch (currentClickState) {
		case ITEM_SELECTED:
			if (currentItemSelected != -1)
				itemUsed(currentItemSelected, new Vector2df(x, y));
			break;
		case DEFAULT:
			// TODO: Notify only elements that are really clicked.

			// inGameClick(e.getPoint());
			break;
		default:
			break;

		}
	}

	/**
	 * Sends an item use event to server.
	 * 
	 * @param i
	 *            item slot.
	 * @param target
	 *            target position
	 */
	void itemUsed(int i, Vector2df target) {
		getListener().onItemUse(i,
				getPanelLogic().computeGuiPointToGameCoordinate(target));
	}

	@Override
	public void itemClicked(int i) {
		if (i >= 0 && i < Inventory.MAX_CAPACITY) {
			getPanelLogic().selectItem(i);
		} else {
			getPanelLogic().setClickState(ClickState.DEFAULT);
			getPanelLogic().getClientData().setCurrentItemSelected(-1);
		}
	}

	private void egoModeMove(int oldx, int oldy, int newx, int newy) {
		getListener().onViewingDirectionChanged(
				getPanelLogic().computeGuiPointToGameCoordinate(
						new Vector2df(newx, newy)));
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		egoModeClick(button, x, y, clickCount);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		egoModeMove(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mousePressed(int button, int x, int y) {
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
	}

	@Override
	public void mouseWheelMoved(int change) {
	}
}
