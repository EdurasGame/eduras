package de.illonis.eduras.mapeditor;

import javax.swing.undo.UndoableEdit;

public abstract class UndoAction implements UndoableEdit {

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		// TODO Auto-generated method stub
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

	@Override
	public String getPresentationName() {
		return "undothing";
	}

	@Override
	public String getUndoPresentationName() {
		return "undo action";
	}

	@Override
	public String getRedoPresentationName() {
		return "redo action";
	}

}
