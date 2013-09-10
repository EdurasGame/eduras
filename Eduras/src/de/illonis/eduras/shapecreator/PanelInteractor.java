package de.illonis.eduras.shapecreator;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import de.illonis.eduras.math.Vector2D;
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
	private Vertice hoverVertice;
	private final DataHolder data;
	private final DrawPanel panel;
	private InteractMode mode = InteractMode.DRAG_EDGE;
	private InteractMode lastMode = InteractMode.NONE;

	public enum InteractMode {
		NONE, DRAG_EDGE, ZOOM, SCROLL, ADD_VERT, REM_VERT;
	}

	public PanelInteractor(DrawPanel panel) {
		this.panel = panel;
		data = DataHolder.getInstance();
	}

	@Override
	public void setMode(InteractMode mode) {
		this.mode = mode;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		GuiPoint guiPoint = new GuiPoint(p.x, p.y);
		Vector2D searchPoint = panel.getCoordinateSystem().guiToCoordinate(
				guiPoint);
		Vertice v = new Vertice(searchPoint.getX(), searchPoint.getY());
		try {
			Vertice vert = data.getPolygon().findNearestVertice(v);
			GuiPoint result = panel.getCoordinateSystem().coordinateToGui(vert);
			if (result.distance(guiPoint) < 10) {
				panel.onVerticeHover(vert);
				hoverVertice = vert;
			} else {
				verticeLeft();
			}
		} catch (NoVerticeFoundException e1) {
			verticeLeft();
		}
	}

	private void verticeLeft() {
		panel.onVerticeLeft();
		hoverVertice = null;
	}

	private boolean trySelectHoverVert() {
		if (hoverVertice != null) {
			panel.selectVertice(hoverVertice);
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
			Vector2D v = panel.getCoordinateSystem().guiToCoordinate(p);
			Vertice vert = new Vertice(v.getX(), v.getY());
			data.getPolygon().addVertice(vert);
			panel.selectVertice(vert);
			break;
		case REM_VERT:
			if (hoverVertice != null) {
				data.getPolygon().removeVertice(hoverVertice);
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
		float zoom = data.getZoom();

		if (e.getWheelRotation() < 0) {
			data.setZoom(zoom + .1f);
		} else {
			data.setZoom(zoom - .1f);
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		Point p = e.getPoint();
		switch (mode) {
		case DRAG_EDGE:
			if (hoverVertice != null) {
				GuiPoint guiPoint = new GuiPoint(p.x, p.y);
				Vector2D coordPoint = panel.getCoordinateSystem()
						.guiToCoordinate(guiPoint);
				hoverVertice.moveTo(coordPoint.getX(), coordPoint.getY());
				panel.selectVertice(hoverVertice);
			}
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
		data.setZoom(zoom);
	}

	@Override
	public void resetPanel() {
		data.setZoom(1.0f);
		panel.centerOrigin();
	}

	@Override
	public void selectVertice(Vertice vert) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean undo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean redo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setShape(EditablePolygon polygon) {
		data.loadPolygon(polygon);
	}
}
