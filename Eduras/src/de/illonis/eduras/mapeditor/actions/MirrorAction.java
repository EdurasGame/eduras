package de.illonis.eduras.mapeditor.actions;

import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.MapPanelLogic;
import de.illonis.eduras.shapecreator.EditablePolygon;

public class MirrorAction extends UndoAction {
	private final int axis;

	public MirrorAction(List<EditorPlaceable> elements, int axis,
			MapPanelLogic logic) {
		super(elements, logic);
		this.axis = axis;
	}

	@Override
	public void undo() throws CannotUndoException {
		performAction();
	}

	@Override
	public void performAction() throws CannotRedoException {
		mirrorElements();
	}

	@Override
	public String getPresentationName() {
		String axisString = (axis == EditablePolygon.X_AXIS) ? "x-axis"
				: "y-axis";
		return "Mirrored " + getElements().size() + " elements at "
				+ axisString;
	}

	@Override
	public String getUndoPresentationName() {
		String axisString = (axis == EditablePolygon.X_AXIS) ? "x-axis"
				: "y-axis";
		return "Undo mirroring of " + getElements().size() + " elements at "
				+ axisString;
	}

	@Override
	public String getRedoPresentationName() {
		String axisString = (axis == EditablePolygon.X_AXIS) ? "x-axis"
				: "y-axis";
		return "Redo mirroring of " + getElements().size() + " elements at "
				+ axisString;
	}

	private void mirrorElements() {
		MapData data = MapData.getInstance();
		float minX = data.getWidth();
		float maxX = 0;
		float minY = data.getHeight();
		float maxY = 0;
		for (EditorPlaceable element : getElements()) {
			minX = Math.min(element.getXPosition(), minX);
			minY = Math.min(element.getYPosition(), minY);
			maxX = Math.max(element.getXPosition() + element.getWidth(), maxX);
			maxY = Math.max(element.getYPosition() + element.getHeight(), maxY);
		}
		float centerX = (maxX + minX) / 2;
		float centerY = (maxY + minY) / 2;
		for (EditorPlaceable element : getElements()) {
			if (axis == EditablePolygon.Y_AXIS) {
				float dx = centerX - element.getXPosition();
				element.setXPosition(element.getXPosition() + 2 * dx
						- element.getWidth());
			}
			if (axis == EditablePolygon.X_AXIS) {
				float dy = centerY - element.getYPosition();
				element.setYPosition(element.getYPosition() + 2 * dy
						- element.getHeight());
			}
			if (element instanceof DynamicPolygonObject) {
				DynamicPolygonObject object = (DynamicPolygonObject) element;
				EditablePolygon poly = EditablePolygon.fromShape(object
						.getShape());
				poly.mirror(axis);
				object.setPolygonVertices(poly.getVector2dfs());
			}
		}
	}

}
