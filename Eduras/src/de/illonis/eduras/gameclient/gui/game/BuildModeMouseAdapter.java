package de.illonis.eduras.gameclient.gui.game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
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

	private void buildModeClick(int button, int x, int y, int clickCount) {
		Vector2f clickGamePoint = getPanelLogic()
				.computeGuiPointToGameCoordinate(new Vector2f(x, y));

		if (button == MouseEvent.BUTTON3) {
			getListener().sendSelectedUnits(clickGamePoint);
		} else if (button == MouseEvent.BUTTON1) {
			getListener().selectOrDeselectAt(clickGamePoint);
		}
	}

	@Override
	public void itemClicked(int slot) {
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

	private Rectangle2D.Double calculateDragRect(Vector2f first, Vector2f second) {
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
	public void mouseClicked(int button, int x, int y, int clickCount) {
		buildModeClick(button, x, y, clickCount);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (getPanelLogic().getClickState() == ClickState.UNITSELECT_DRAGGING) {
			Point first = startPoint;
			Point second = new Point(newx, newy);
			getPanelLogic().getDragRect().setRectangle(
					calculateDrawRect(first, second));
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == MouseEvent.BUTTON1) {
			getPanelLogic().setClickState(ClickState.UNITSELECT_DRAGGING);
			startPoint = new Point(x, y);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == MouseEvent.BUTTON1) {
			getPanelLogic().setClickState(ClickState.DEFAULT);

			Vector2f start = getPanelLogic().computeGuiPointToGameCoordinate(
					new Vector2f(startPoint.x, startPoint.y));
			Vector2f end = getPanelLogic().computeGuiPointToGameCoordinate(
					new Vector2f(x, y));
			Rectangle2D.Double r = calculateDragRect(start, end);
			getListener().onUnitsSelected(r);
			getPanelLogic().getDragRect().clear();
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
	}

}
