package de.illonis.eduras.mapeditor.actions;

import java.util.LinkedList;
import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoableEdit;

import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapPanelLogic;

public abstract class UndoAction implements UndoableEdit {

	private final LinkedList<EditorPlaceable> elements;
	protected final MapPanelLogic logic;

	public UndoAction(List<EditorPlaceable> elements, MapPanelLogic logic) {
		this.elements = new LinkedList<EditorPlaceable>(elements);
		this.logic = logic;
	}

	protected LinkedList<EditorPlaceable> getElements() {
		return elements;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	@Override
	public final void redo() throws CannotRedoException {
		performAction();
	}

	@Override
	public void die() {
	}

	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		return false;
	}

	@Override
	public boolean replaceEdit(UndoableEdit anEdit) {
		return false;
	}

	@Override
	public boolean isSignificant() {
		return true;
	}

	public abstract void performAction() throws CannotRedoException;

}
