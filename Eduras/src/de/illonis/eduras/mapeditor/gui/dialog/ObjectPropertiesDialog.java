package de.illonis.eduras.mapeditor.gui.dialog;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.gameobjects.TriggerArea;
import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;

/**
 * A dialog for object properties.
 * 
 * @author illonis
 * 
 */
public class ObjectPropertiesDialog extends PropertiesDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a properties dialog for a node.
	 * 
	 * @param parent
	 *            the parent window.
	 * @param node
	 *            the node.
	 */
	public ObjectPropertiesDialog(EditorWindow parent, NodeData node) {
		this(parent, (EditorPlaceable) node);
		this.node = node;
		addNeutralBaseTab();
	}

	/**
	 * Create a properties dialog for a spawnarea.
	 * 
	 * @param window
	 *            parent window.
	 * @param spawn
	 *            the spawnarea.
	 */
	public ObjectPropertiesDialog(EditorWindow window, SpawnPosition spawn) {
		this(window, (EditorPlaceable) spawn);
		this.spawn = spawn;
		addSpawnTab();
	}

	/**
	 * Creates a properties dialog for a gameobject.
	 * 
	 * @param parent
	 *            parent window.
	 * @param object
	 *            the object.
	 */
	public ObjectPropertiesDialog(EditorWindow parent, GameObject object) {
		this(parent, (EditorPlaceable) object);
		this.object = object;
		addPropertiesTab();

		if (object instanceof DynamicPolygonObject) {
			addColorTab(((DynamicPolygonObject) object).getColor());
			addTextureTab(object.getTexture());
		} else if (object instanceof Portal) {
			addPortalTab();
		}
	}

	private ObjectPropertiesDialog(EditorWindow parent, EditorPlaceable element) {
		super(parent);
		entity = element;
		addGeneralTab();
		if (entity instanceof TriggerArea || entity instanceof SpawnPosition
				|| entity instanceof NodeData) {
			addSizeTab();
		}
	}
}
