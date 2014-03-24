package de.illonis.eduras.gameclient.gui.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.math.Vector2df;
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
		Vector2df clickGamePoint = getPanelLogic()
				.computeGuiPointToGameCoordinate(new Vector2df(e.getPoint()));

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

			Vector2df start = getPanelLogic().computeGuiPointToGameCoordinate(
					new Vector2df(startPoint));
			Vector2df end = getPanelLogic().computeGuiPointToGameCoordinate(
					new Vector2df(e.getPoint()));
			Rectangle2D.Double r = calculateDragRect(start, end);
			getListener().onUnitsSelected(r);
			getPanelLogic().getDragRect().clear();
		}
	}

	private Rectangle calculateDrawRect(Point first, Point second) {
		if (first.x == second.x || first.y == second.y)
			return new Rectangle(first.x, first.y, 0, 0);

		int topLeftX = Math.min(first.x, second.x);
		int topLeftY = Math.min(first.y, second.y);
		int bottomRightX = Math.max(first.x, second.x);
		int bottomRightY = Math.max(first.y, second.y);

		return new Rectangle(topLeftX, topLeftY, bottomRightX - topLeftX,
				bottomRightY - topLeftY);
	}

	private Rectangle2D.Double calculateDragRect(Vector2df first, Vector2df second) {
		if (first.getX() == second.getX() || first.getY() == second.getY())
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
		if (getPanelLogic().getClickState() == ClickState.UNITSELECT_DRAGGING) {
			Point first = startPoint;
			Point second = e.getPoint();
			getPanelLogic().getDragRect().setRectangle(
					calculateDrawRect(first, second));
		}
	}

}
