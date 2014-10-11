package de.illonis.eduras.mapeditor.actions;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.MapPanelLogic;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;

public class DeleteAction extends UndoAction {

	public DeleteAction(List<EditorPlaceable> elements, MapPanelLogic logic) {
		super(elements, logic);
	}

	@Override
	public void undo() throws CannotUndoException {
		MapData data = MapData.getInstance();
		boolean unsupported = false;
		for (EditorPlaceable element : getElements()) {
			data.add(element);
			if (element instanceof NodeData || element instanceof Portal) {
				unsupported = true;
			}
		}
		logic.selectAll(getElements());
		if (unsupported)
			JOptionPane
					.showMessageDialog(null,
							"Could not recreate all connections of deleted nodes and portals.");
	}

	@Override
	public String getPresentationName() {
		return "Deleted " + getElements().size() + " elements.";
	}

	@Override
	public String getUndoPresentationName() {
		return "Create " + getElements().size() + " deleted elements.";
	}

	@Override
	public String getRedoPresentationName() {
		return "Delete " + getElements().size() + " elements.";
	}

	@Override
	public void performAction() throws CannotRedoException {
		MapData data = MapData.getInstance();
		for (EditorPlaceable element : getElements()) {
			if (element instanceof GameObject) {
				GameObject o = (GameObject) element;
				data.remove(o);
				if (o instanceof Portal) {
					Portal portal = (Portal) o;
					for (GameObject obj : data.getGameObjects()) {
						if (obj instanceof Portal) {
							Portal parent = (Portal) obj;
							if (parent.getPartnerPortal() != null
									&& portal.equals(parent.getPartnerPortal())) {
								parent.setPartnerPortal(null);
							}
						}
					}
				}
			} else if (element instanceof NodeData) {
				NodeData node = (NodeData) element;
				for (NodeData subNode : data.getBases()) {
					subNode.getAdjacentNodes().remove(node);
				}
				data.remove(node);
			} else if (element instanceof SpawnPosition) {
				SpawnPosition spawn = (SpawnPosition) element;

				data.remove(spawn);
			}
		}
		logic.clearSelection();
	}
}
