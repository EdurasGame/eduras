package de.illonis.eduras.mapeditor;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.InputAdapter;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.EditablePolygon;
import de.illonis.eduras.shapecreator.NoVerticeFoundException;

/**
 * Handles user interaction in shape editing mode.
 * 
 * @author illonis
 * 
 */
public class ShapeEditInputHandler extends InputAdapter {

	private final MapInteractor interactor;
	private final MapData data;
	private boolean dragging;

	private boolean addBefore = false;
	private Vector2f nearest;

	private Vector2f hover;

	ShapeEditInputHandler(MapInteractor panelLogic) {
		this.interactor = panelLogic;
		dragging = false;
		data = MapData.getInstance();
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		Vector2f point = interactor
				.computeGuiPointToGameCoordinate(new Vector2f(newx, newy));
		try {
			Vector2f vert = data.getEditShape().findNearestVector2df(point);
			nearest = vert;
			if (vert.distance(point) < 10) {
				data.clearTempLines();
				hover = vert;
			} else {
				hover = null;
				Vector2f before = data.getEditShape().findBefore(vert);
				Vector2f after = data.getEditShape().findAfter(vert);
				float afterDistance = after.distance(point);
				float beforeDistance = before.distance(point);
				data.setTempLineA(new Line(point, vert));
				Line l;
				if (afterDistance < beforeDistance) {
					l = new Line(point, after);
					data.setRemovedLine(new Line(vert, after));
					addBefore = false;
				} else {
					l = new Line(point, before);
					data.setRemovedLine(new Line(vert, before));
					addBefore = true;
				}
				data.setTempLineB(l);

			}
		} catch (NoVerticeFoundException e) {
		}
	}

	private boolean trySelectHoverVert() {
		if (hover != null) {
			return true;
		}
		return false;
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (dragging && hover != null) {
			Vector2f coordPoint = interactor
					.computeGuiPointToGameCoordinate(new Vector2f(newx, newy));
			hover.set(coordPoint.x, coordPoint.y);
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (hover != null && button == Input.MOUSE_LEFT_BUTTON)
			dragging = true;
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (dragging) {
			hover = null;
			dragging = false;
		} else {
			switch (button) {
			case Input.MOUSE_LEFT_BUTTON:
				if (trySelectHoverVert())
					break;
				Vector2f gamePos = interactor
						.computeGuiPointToGameCoordinate(new Vector2f(x, y));
				Vector2df vert = new Vector2df(gamePos.x, gamePos.y);
				if (addBefore) {
					data.getEditShape().addVerticeBefore(vert, nearest);
				} else {
					data.getEditShape().addVerticeAfter(vert, nearest);
				}
				break;
			case Input.MOUSE_RIGHT_BUTTON:
				if (hover != null) {
					data.getEditShape().removeVector2df(hover);
					hover = null;
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_A:
			interactor.stopScrolling(Direction.LEFT);
			break;
		case Input.KEY_D:
			interactor.stopScrolling(Direction.RIGHT);
			break;
		case Input.KEY_W:
			interactor.stopScrolling(Direction.TOP);
			break;
		case Input.KEY_S:
			interactor.stopScrolling(Direction.BOTTOM);
			break;
		case Input.KEY_M:
			data.getEditShape().mirror(EditablePolygon.X_AXIS);
			break;
		case Input.KEY_N:
			data.getEditShape().mirror(EditablePolygon.Y_AXIS);
			break;
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		boolean rotated = false;
		if (interactor.getInput().isKeyDown(Input.KEY_LCONTROL)) {
			rotated = true;
			float amount;
			if (change < 0) {
				amount = 1f;
			} else {
				amount = -1f;
			}
			if (interactor.getInput().isKeyDown(Input.KEY_LSHIFT)) {
				amount *= 10;
			}
			data.getEditShape().rotate(amount);
		}
		if (!rotated) {
			float amount;
			if (change > 0) {
				amount = 0.1f;
			} else {
				amount = -0.1f;
			}
			interactor.setZoom(interactor.getZoom() + amount);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_A:
			interactor.startScrolling(Direction.LEFT);
			break;
		case Input.KEY_D:
			interactor.startScrolling(Direction.RIGHT);
			break;
		case Input.KEY_W:
			interactor.startScrolling(Direction.TOP);
			break;
		case Input.KEY_S:
			interactor.startScrolling(Direction.BOTTOM);
			break;
		case Input.KEY_V:
			List<Vector2f> verts = new LinkedList<Vector2f>(data.getEditShape()
					.getVector2dfs());
			Vector2f[] vertices = new Vector2f[verts.size()];
			for (int i = 0; i < verts.size(); i++) {
				vertices[i] = verts.get(i);
			}
			data.getEditObject().setPolygonVertices(vertices);
			interactor.setInteractType(InteractType.DEFAULT);
			break;
		default:
			break;
		}
	}
}
