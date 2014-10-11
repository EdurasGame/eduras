package de.illonis.eduras.mapeditor.actions;

import java.util.LinkedList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapPanelLogic;

public class DragAction extends UndoAction {
	private final Vector2f diff;

	public DragAction(LinkedList<EditorPlaceable> elements, Vector2f distance,
			MapPanelLogic logic) {
		super(elements, logic);
		this.diff = distance;
	}

	@Override
	public void undo() throws CannotUndoException {
		for (EditorPlaceable element : getElements()) {
			element.setPosition(element.getXPosition() - diff.x,
					element.getYPosition() - diff.y);
		}
	}

	@Override
	public void performAction() throws CannotRedoException {
		for (EditorPlaceable element : getElements()) {
			element.setPosition(element.getXPosition() + diff.x,
					element.getYPosition() + diff.y);
		}
	}

	@Override
	public String getPresentationName() {
		return "Moved " + getElements().size() + " elements.";
	}

	@Override
	public String getUndoPresentationName() {
		return "Undo moving of " + getElements().size() + " elements.";
	}

	@Override
	public String getRedoPresentationName() {
		return "Redo moving of " + getElements().size() + " elements.";
	}
}
