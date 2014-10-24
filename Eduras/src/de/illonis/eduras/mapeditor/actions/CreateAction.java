package de.illonis.eduras.mapeditor.actions;

import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.MapPanelLogic;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;

public class CreateAction extends UndoAction {
	private NodeData node;
	private SpawnPosition spawn;
	private GameObject object;

	public CreateAction(NodeData node, MapPanelLogic logic) {
		this((EditorPlaceable) node, logic);
		this.node = node;
	}

	public CreateAction(SpawnPosition spawn, MapPanelLogic logic) {
		this((EditorPlaceable) spawn, logic);
		this.spawn = spawn;
	}

	public CreateAction(GameObject object, MapPanelLogic logic) {
		this((EditorPlaceable) object, logic);
		this.object = object;
	}

	public CreateAction(EditorPlaceable element, MapPanelLogic mapPanelLogic) {
		super(new LinkedList<EditorPlaceable>(Arrays.asList(element)),
				mapPanelLogic);
	}

	@Override
	public void undo() throws CannotUndoException {
		if (node != null)
			MapData.getInstance().remove(node);
		else if (spawn != null) {
			MapData.getInstance().remove(spawn);
		} else if (object != null) {
			MapData.getInstance().remove(object);
		} else
			throw new CannotRedoException();
		if (logic.getSelectedElements().contains(getElements().getFirst()))
			logic.clearSelection();
	}

	@Override
	public void performAction() throws CannotRedoException {
		if (node != null)
			MapData.getInstance().addBase(node);
		else if (spawn != null) {
			MapData.getInstance().addSpawnPoint(spawn);
		} else if (object != null) {
			MapData.getInstance().addGameObject(object);
		} else
			throw new CannotRedoException();
		logic.select(getElements().getFirst());
	}

	@Override
	public String getPresentationName() {
		return "Created " + getElements().size() + " elements.";
	}

	@Override
	public String getUndoPresentationName() {
		return "Delete " + getElements().size() + " created elements.";
	}

	@Override
	public String getRedoPresentationName() {
		return "Create " + getElements().size() + " elements.";
	}
}
