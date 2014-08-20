package de.illonis.eduras.shapecreator;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.gui.DrawPanel;
import de.illonis.eduras.shapecreator.gui.GuiPoint;

/**
 * Handles all mouse-related events and actions on the drawing panel.
 * 
 * @author illonis
 * 
 */
public class PanelInteractor extends MouseAdapter implements PanelModifier {
	private static final Cursor CURSOR_SCROLL = new Cursor(
			Cursor.CROSSHAIR_CURSOR);
	private static final Cursor CURSOR_ZOOM = new Cursor(
			Cursor.NE_RESIZE_CURSOR);
	private Point clickPoint;
	private Vector2f hoverVector2df;
	private final DataHolder data;
	private final DrawPanel panel;
	private InteractMode mode = InteractMode.DRAG_EDGE;
	private InteractMode lastMode = InteractMode.NONE;
	private boolean addBefore = false;
	private Vector2f nearest;

	/**
	 * Describes the mode that is used to interact with the panel.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum InteractMode {
		NONE, DRAG_EDGE, ZOOM, SCROLL, ADD_VERT, REM_VERT, DRAG_SHAPE,
		SCALE_SHAPE;
	}

	PanelInteractor(DrawPanel panel) {
		this.panel = panel;
		data = DataHolder.getInstance();
	}

	@Override
	public void setMode(InteractMode mode) {
		this.mode = mode;
		data.clearTempLines();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		GuiPoint guiPoint = new GuiPoint(p.x, p.y);
		Vector2f searchPoint = panel.getCoordinateSystem().guiToCoordinate(
				guiPoint);
		Vector2f v = new Vector2df(searchPoint.getX(), searchPoint.getY());
		try {
			Vector2f vert = data.getPolygon().findNearestVector2df(v);
			GuiPoint result = panel.getCoordinateSystem().coordinateToGui(vert);
			nearest = vert;
			if (result.distance(guiPoint) < 10) {
				data.clearTempLines();
				panel.onVector2dfHover(vert);
				hoverVector2df = vert;
			} else {
				verticeLeft();
				if (mode == InteractMode.ADD_VERT) {
					Vector2f before = data.getPolygon().findBefore(vert);
					Vector2f after = data.getPolygon().findAfter(vert);
					float afterDistance = after.distance(searchPoint);
					float beforeDistance = before.distance(searchPoint);
					data.setTempLineA(new Line(searchPoint, vert));
					Line l;
					if (afterDistance < beforeDistance) {
						l = new Line(searchPoint, after);
						addBefore = false;
					} else {
						l = new Line(searchPoint, before);
						addBefore = true;
					}
					data.setTempLineB(l);
				}
			}
		} catch (NoVerticeFoundException e1) {
			verticeLeft();
			data.clearTempLines();
		}
	}

	private void verticeLeft() {
		panel.onVector2dfLeft();
		hoverVector2df = null;
	}

	private boolean trySelectHoverVert() {
		if (hoverVector2df != null) {
			panel.selectVector2df(hoverVector2df);
			return true;
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		switch (mode) {
		case ADD_VERT:
			if (trySelectHoverVert())
				break;
			GuiPoint p = new GuiPoint(clickPoint.x, clickPoint.y);
			Vector2df v = panel.getCoordinateSystem().guiToCoordinate(p);
			Vector2df vert = new Vector2df(v.getX(), v.getY());
			if (addBefore) {
				data.getPolygon().addVerticeBefore(vert, nearest);
			} else {
				data.getPolygon().addVerticeAfter(vert, nearest);
			}
			panel.selectVector2df(vert);
			break;
		case REM_VERT:
			if (hoverVector2df != null) {
				data.getPolygon().removeVector2df(hoverVector2df);
			}
			break;

		default:
			trySelectHoverVert();
			break;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		clickPoint = e.getPoint();
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			switch (mode) {
			case REM_VERT:
			case NONE:
			case ADD_VERT:
				lastMode = mode;
				mode = InteractMode.DRAG_EDGE;
				break;
			default:
				break;
			}

			break;
		case MouseEvent.BUTTON2:
			lastMode = mode;
			mode = InteractMode.ZOOM;
			break;
		case MouseEvent.BUTTON3:
			lastMode = mode;
			mode = InteractMode.SCROLL;
			panel.setCursor(CURSOR_SCROLL);
			break;
		default:
			mode = InteractMode.NONE;
			return;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			switch (mode) {
			case DRAG_EDGE:
				mode = lastMode;
				break;
			default:
				break;
			}

			break;
		case MouseEvent.BUTTON2:
			if (mode == InteractMode.ZOOM)
				mode = lastMode;
			break;
		case MouseEvent.BUTTON3:
			if (mode == InteractMode.SCROLL)
				mode = lastMode;
			break;
		}
		panel.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		panel.setCursor(CURSOR_ZOOM);
		float zoom = panel.getCoordinateSystem().getZoom();

		if (e.getWheelRotation() < 0) {
			panel.getCoordinateSystem().setZoom(zoom + .1f);
		} else {
			panel.getCoordinateSystem().setZoom(zoom - .1f);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		Point p = e.getPoint();
		switch (mode) {
		case DRAG_EDGE:
			if (hoverVector2df != null) {
				GuiPoint guiPoint = new GuiPoint(p.x, p.y);
				Vector2df coordPoint = panel.getCoordinateSystem()
						.guiToCoordinate(guiPoint);
				hoverVector2df.set(coordPoint.getX(), coordPoint.getY());
				data.notifyVector2dfsChanged();
				panel.selectVector2df(hoverVector2df);
			}
			break;
		case DRAG_SHAPE:
			GuiPoint guiPoint = new GuiPoint(p.x, p.y);
			GuiPoint formerPoint = new GuiPoint(clickPoint.x, clickPoint.y);

			Vector2f oldPoint = panel.getCoordinateSystem().guiToCoordinate(
					formerPoint);

			Vector2f newPoint = panel.getCoordinateSystem().guiToCoordinate(
					guiPoint);
			float dx = newPoint.getX() - oldPoint.getX();
			float dy = newPoint.getY() - oldPoint.getY();
			Vector2f diffVector = new Vector2df(dx, dy);
			for (Vector2f v : getShape().getVector2dfs()) {
				v.add(diffVector);
			}
			data.notifyVector2dfsChanged();
			clickPoint = guiPoint;
			break;
		case SCALE_SHAPE:
			GuiPoint point = new GuiPoint(p.x, p.y);
			GuiPoint point2 = new GuiPoint(clickPoint.x, clickPoint.y);
			GuiPoint origin = panel.getCoordinateSystem().getOrigin();
			double d1 = GuiPoint.distance(point.x, point.y, origin.x, origin.y);
			double d2 = GuiPoint.distance(point2.x, point2.y, origin.x,
					origin.y);

			float multipler = 1;
			if (d1 < d2) {
				multipler = 0.9f;
			} else
				multipler = 1.1f;
			for (Vector2f v : getShape().getVector2dfs()) {
				v.scale(multipler);
			}
			data.notifyVector2dfsChanged();
			clickPoint = point;
			break;
		case SCROLL:
			int xDiff = e.getX() - clickPoint.x;
			int yDiff = e.getY() - clickPoint.y;
			clickPoint = p;
			panel.getCoordinateSystem().getOrigin().translate(xDiff, yDiff);
			break;
		default:
			break;
		}

	}

	@Override
	public void setZoom(float zoom) {
		panel.getCoordinateSystem().setZoom(zoom);
	}

	@Override
	public void modZoom(float modifier) {
		panel.getCoordinateSystem().setZoom(
				panel.getCoordinateSystem().getZoom() + modifier);
	}

	@Override
	public void resetPanel() {
		panel.getCoordinateSystem().setZoom(1.0f);
		panel.centerOrigin();
	}

	@Override
	public void selectVector2df(Vector2df vert) {
		panel.onVector2dfSelected(vert);
	}

	@Override
	public boolean undo() {
		// TODO implement.
		return false;
	}

	@Override
	public boolean redo() {
		// TODO implement.
		return false;
	}

	@Override
	public void setShape(EditablePolygon polygon) {
		data.loadPolygon(polygon);
	}

	@Override
	public EditablePolygon getShape() {
		return data.getPolygon();
	}

	@Override
	public void setBackgroundImage(Image image) {
		data.setBackgroundImage(image);
	}
}
