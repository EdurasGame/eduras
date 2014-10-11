package de.illonis.eduras.mapeditor.actions;

import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapPanelLogic;

public class ShapeEditAction extends UndoAction {

	public ShapeEditAction(DynamicPolygonObject object,
			Vector2f[] previousShape, MapPanelLogic logic) {
		super(new LinkedList<EditorPlaceable>(Arrays.asList(object)), logic);
	}

	@Override
	public void undo() throws CannotUndoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void performAction() throws CannotRedoException {
		// TODO Auto-generated method stub

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
