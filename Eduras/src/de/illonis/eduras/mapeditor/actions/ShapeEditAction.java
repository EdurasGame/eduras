package de.illonis.eduras.mapeditor.actions;

import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapPanelLogic;
import de.illonis.eduras.shapecreator.EditablePolygon;

public class ShapeEditAction extends UndoAction {
	private final EditablePolygon previousShape;
	private final Vector2f[] currentShape;

	public ShapeEditAction(DynamicPolygonObject object,
			EditablePolygon previousShape, MapPanelLogic logic) {
		super(new LinkedList<EditorPlaceable>(Arrays.asList(object)), logic);
		this.previousShape = previousShape;
		this.currentShape = object.getPolygonVertices();
	}

	@Override
	public void undo() throws CannotUndoException {
		DynamicPolygonObject object = ((DynamicPolygonObject) getElements()
				.getFirst());
		object.setPolygonVertices(previousShape.getVector2dfs());
	}

	@Override
	public void performAction() throws CannotRedoException {
		DynamicPolygonObject object = ((DynamicPolygonObject) getElements()
				.getFirst());
		object.setPolygonVertices(currentShape);
	}

	@Override
	public String getPresentationName() {
		return "Changed a shape's shape.";
	}

	@Override
	public String getUndoPresentationName() {
		return "Revert edit of shape";
	}

	@Override
	public String getRedoPresentationName() {
		return "Redo edit of shape";
	}
}
