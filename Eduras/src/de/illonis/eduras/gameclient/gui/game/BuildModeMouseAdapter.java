package de.illonis.eduras.gameclient.gui.game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * Handles mouse events in the build mode ({@link InteractMode#MODE_STRATEGY}).
 * 
 * @author illonis
 * 
 */
public class BuildModeMouseAdapter extends GuiMouseAdapter {

	private Point startPoint;

	protected BuildModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);
	}

	private void buildModeClick(MouseEvent e) {
		Vector2D clickGamePoint = getPanelLogic()
				.computeGuiPointToGameCoordinate(new Vector2D(e.getPoint()));

		if (e.getButton() == MouseEvent.BUTTON3) {
			getListener().sendSelectedUnits(clickGamePoint);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			getListener().selectOrDeselectAt(clickGamePoint);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		buildModeClick(e);
	}

	@Override
	public void itemClicked(int slot) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			getPanelLogic().setClickState(ClickState.UNITSELECT_DRAGGING);
			startPoint = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			getPanelLogic().setClickState(ClickState.DEFAULT);

			Vector2D start = getPanelLogic().computeGuiPointToGameCoordinate(
					new Vector2D(startPoint));
			Vector2D end = getPanelLogic().computeGuiPointToGameCoordinate(
					new Vector2D(e.getPoint()));
			Rectangle2D.Double r = calculateDragRect(start, end);
			getListener().onUnitsSelected(r);
		}
	}

	private Rectangle2D.Double calculateDragRect(Vector2D first, Vector2D second) {
		if (first.getX() == second.getX() || first.getX() == second.getY())
			return new Rectangle2D.Double(first.getX(), first.getY(), 0, 0);

		double topLeftX = Math.min(first.getX(), second.getX());
		double topLeftY = Math.min(first.getY(), second.getY());
		double bottomRightX = Math.max(first.getX(), second.getX());
		double bottomRightY = Math.max(first.getY(), second.getY());

		return new Rectangle2D.Double(topLeftX, topLeftY, bottomRightX
				- topLeftX, bottomRightY - topLeftY);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {

		}
	}

}
