package de.illonis.eduras.gameclient.gui.game;

import java.awt.event.MouseEvent;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.math.Vector2D;
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

	private void egoModeClick(MouseEvent e) {
		ClickState currentClickState = getPanelLogic().getClickState();
		int currentItemSelected = getPanelLogic().getClientData()
				.getCurrentItemSelected();

		switch (currentClickState) {
		case ITEM_SELECTED:
			if (currentItemSelected != -1)
				itemUsed(currentItemSelected, new Vector2D(e.getPoint()));
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
	void itemUsed(int i, Vector2D target) {
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

	@Override
	public void mouseClicked(MouseEvent e) {
		egoModeClick(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);

		egoModeMove(e);
	}

	private void egoModeMove(MouseEvent e) {
		getListener().onViewingDirectionChanged(
				getPanelLogic().computeGuiPointToGameCoordinate(
						new Vector2D(e.getPoint().x, e.getPoint().y)));
	}
}
