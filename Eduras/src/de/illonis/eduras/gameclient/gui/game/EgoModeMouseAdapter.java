package de.illonis.eduras.gameclient.gui.game;

import java.awt.event.MouseEvent;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.math.Vector2D;

public class EgoModeMouseAdapter extends GuiMouseAdapter {

	private int currentItemSelected = -1;

	public EgoModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);

	}

	private void egoModeClick(MouseEvent e) {
		ClickState currentClickState = getPanelLogic().getClickState();
		switch (currentClickState) {
		case ITEM_SELECTED:
			if (currentItemSelected != -1)
				itemUsed(currentItemSelected, new Vector2D(e.getPoint()));
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
		System.out.println("item use");
		getListener().onItemUse(i,
				getPanelLogic().computeGuiPointToGameCoordinate(target));
	}

	@Override
	public void itemClicked(int i) {
		if (i >= 0 && i < Inventory.MAX_CAPACITY) {
			currentItemSelected = i;
			getPanelLogic().setClickState(ClickState.ITEM_SELECTED);
		} else {
			getPanelLogic().setClickState(ClickState.DEFAULT);
			currentItemSelected = -1;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		egoModeClick(e);
	}

}
