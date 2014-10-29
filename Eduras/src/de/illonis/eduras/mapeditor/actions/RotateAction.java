package de.illonis.eduras.mapeditor.actions;

import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapPanelLogic;
import de.illonis.eduras.shapecreator.EditablePolygon;

public class RotateAction extends UndoAction {
	private final float degrees;

	public RotateAction(List<EditorPlaceable> elements, float degrees,
			MapPanelLogic logic) {
		super(elements, logic);
		this.degrees = degrees;
	}

	@Override
	public void undo() throws CannotUndoException {
		for (EditorPlaceable element : getElements()) {
			if (element instanceof DynamicPolygonObject) {
				DynamicPolygonObject object = (DynamicPolygonObject) element;
				EditablePolygon poly = EditablePolygon.fromShape(object
						.getShape());
				poly.rotate(-degrees);
				object.setPolygonVertices(poly.getVector2dfs());
			}
		}
	}

	@Override
	public void performAction() throws CannotRedoException {
		for (EditorPlaceable element : getElements()) {
			if (element instanceof DynamicPolygonObject) {
				DynamicPolygonObject object = (DynamicPolygonObject) element;
				EditablePolygon poly = EditablePolygon.fromShape(object
						.getShape());
				poly.rotate(degrees);
				object.setPolygonVertices(poly.getVector2dfs());
			}
		}
	}

	@Override
	public String getPresentationName() {
		return "Rotate " + getElements().size() + "Element(s) by " + degrees
				+ "\u00b0";
	}

	@Override
	public String getUndoPresentationName() {
		return "Undo rotation of " + getElements().size() + "Element(s) by "
				+ degrees + "\u00b0";
	}

	@Override
	public String getRedoPresentationName() {
		return "Redo rotation of " + getElements().size() + "Element(s) by "
				+ degrees + "\u00b0";
	}
}
