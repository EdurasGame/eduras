package de.illonis.eduras.mapeditor;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.FactoryException;
import de.illonis.eduras.ObjectCreator;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.mapeditor.gui.dialog.PropertiesDialog;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.shapecreator.EditablePolygon;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapes.data.ShapeParser;

/**
 * Manages access to edited map.
 * 
 * @author illonis
 * 
 */
public class MapPanelLogic implements MapInteractor {

	private int nextId = 1;

	private final GameCamera viewPort;
	private InteractType interactType;
	private float zoom = 1f;
	private Vector2f scrollVector;
	private final static float SCROLL_SPEED = 5;
	private final MapData data = MapData.getInstance();
	private ObjectType currentSpawnType = ObjectType.NO_OBJECT;
	private final EditorWindow window;
	private Input input;
	private List<EditorPlaceable> selectedElements;
	private Rectangle dragRect;

	MapPanelLogic(EditorWindow window) {
		this.window = window;
		selectedElements = new LinkedList<EditorPlaceable>();
		interactType = InteractType.DEFAULT;
		viewPort = new GameCamera();
		viewPort.setSize(800, 600);
		scrollVector = new Vector2f();
		dragRect = new Rectangle(0, 0, 0, 0);
	}

	/**
	 * Sets input to provide mouse location.
	 * 
	 * @param input
	 *            the input.
	 */
	public void setInput(Input input) {
		this.input = input;
	}

	@Override
	public Vector2f computeGuiPointToGameCoordinate(Vector2f v) {
		float scale = zoom;
		Vector2f vec = new Vector2f(v);
		vec.x += viewPort.getX();
		vec.y += viewPort.getY();
		// vec.x /= gui.getContainer().getWidth();
		// vec.y /= gui.getContainer().getHeight();
		vec.scale(1 / scale);
		return vec;
	}

	@Override
	public void scroll(int x, int y) {
		viewPort.setX(viewPort.getX() + x);
		viewPort.setY(viewPort.getY() + y);
	}

	@Override
	public GameCamera getViewPort() {
		return viewPort;
	}

	@Override
	public float getZoom() {
		return zoom;
	}

	@Override
	public void startScrolling(Direction dir) {
		switch (dir) {
		case BOTTOM:
			scrollVector.y = SCROLL_SPEED;
			break;
		case TOP:
			scrollVector.y = -SCROLL_SPEED;
			break;
		case LEFT:
			scrollVector.x = -SCROLL_SPEED;
			break;
		case RIGHT:
			scrollVector.x = SCROLL_SPEED;
			break;
		default:
			break;
		}
	}

	@Override
	public void stopScrolling(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			scrollVector.x = 0;
		else if (dir == Direction.TOP || dir == Direction.BOTTOM)
			scrollVector.y = 0;
	}

	/**
	 * logic updates
	 * 
	 * @param delta
	 *            time span.
	 */
	public void update(int delta) {
		float x = viewPort.getX();
		float y = viewPort.getY();
		viewPort.setLocation(x + scrollVector.x, scrollVector.y + y);
	}

	@Override
	public void showPropertiesOfSelected() {
		if (selectedElements.isEmpty())
			return;
		PropertiesDialog dialog = null;
		EditorPlaceable obj = selectedElements.get(0);
		if (obj instanceof GameObject) {
			dialog = new PropertiesDialog(window, (GameObject) obj);
		} else if (obj instanceof NodeData) {
			dialog = new PropertiesDialog(window, (NodeData) obj);
		} else if (obj instanceof SpawnPosition) {
			dialog = new PropertiesDialog(window, (SpawnPosition) obj);
		}
		if (dialog != null)
			dialog.setVisible(true);
	}

	@Override
	public boolean isObjectAt(int guiX, int guiY) {
		return (getObjectAt(guiX, guiY) != null);
	}

	@Override
	public boolean isBaseAt(int guiX, int guiY) {
		return (getBaseAt(guiX, guiY) != null);
	}

	@Override
	public boolean isSpawnPointAt(int guiX, int guiY) {
		return (getSpawnPointAt(guiX, guiY) != null);
	}

	private GameObject getObjectAt(int guiX, int guiY) {
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		for (GameObject o : data.getGameObjects()) {
			if (o.getShape().contains(mapPos.x, mapPos.y)) {
				return o;
			}
		}
		return null;
	}

	@Override
	public void setSpawnType(ObjectType selectedValue) {
		currentSpawnType = selectedValue;
	}

	@Override
	public void spawnAt(int guiX, int guiY) {
		if (currentSpawnType != ObjectType.NO_OBJECT) {
			try {
				GameObject o = ObjectCreator.createObject(currentSpawnType,
						null, null);
				if (o.getShape() != null) {
					float width = o.getShape().getWidth() * zoom;
					float height = o.getShape().getHeight() * zoom;
					guiX -= width / 2;
					guiY -= height / 2;
				}
				Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(
						guiX, guiY));
				o.setPosition(mapPos.x, mapPos.y);
				o.setId(nextId++);
				data.addGameObject(o);
				select(o);
				if (o instanceof Portal) {
					o.setRefName("Portal" + o.getId());
				}
			} catch (FactoryException | ShapeVerticesNotApplicableException e) {
				JOptionPane.showMessageDialog(window, "Object "
						+ currentSpawnType + " not supported");
			}
		}
	}

	private void select(EditorPlaceable o) {
		clearSelection();
		selectedElements.add(o);
	}

	private NodeData getBaseAt(int guiX, int guiY) {
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		for (NodeData o : data.getBases()) {
			Rectangle r = new Rectangle(o.getXPosition(), o.getYPosition(),
					o.getWidth(), o.getHeight());
			if (r.contains(mapPos.x, mapPos.y)) {
				return o;
			}
		}
		return null;
	}

	private SpawnPosition getSpawnPointAt(int guiX, int guiY) {
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		for (SpawnPosition o : data.getSpawnPoints()) {
			if (o.getArea().contains(mapPos.x, mapPos.y)) {
				return o;
			}
		}
		return null;
	}

	@Override
	public void createBaseAt(int guiX, int guiY) {
		float width = NeutralArea.DEFAULT_SIZE;
		float height = NeutralArea.DEFAULT_SIZE;
		guiX -= width * zoom / 2;
		guiY -= height * zoom / 2;
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		NodeData node = new NodeData(mapPos.x, mapPos.y, nextId++,
				new LinkedList<NodeData>(), BaseType.NEUTRAL, "");
		data.addBase(node);
		select(node);
	}

	@Override
	public void createSpawnPointAt(int guiX, int guiY) {
		float width = 40;
		float height = 40;
		guiX -= width * zoom / 2;
		guiY -= height * zoom / 2;
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		SpawnPosition spawn = new SpawnPosition(new Rectangle(mapPos.x,
				mapPos.y, 40, 40), SpawnType.ANY);
		data.addSpawnPoint(spawn);
		select(spawn);
	}

	@Override
	public void setInteractType(InteractType type) {
		interactType = type;
		if (interactType == InteractType.DEFAULT) {
			currentSpawnType = ObjectType.NO_OBJECT;
			data.setPlacingObject(null);
		}
	}

	@Override
	public InteractType getInteractType() {
		return interactType;
	}

	@Override
	public void placeShapeAt(int guiX, int guiY) {
		DynamicPolygonObject shape = data.getPlacingObject();
		shape.setId(nextId++);
		data.addGameObject(shape);
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		shape.setPosition(mapPos.x - shape.getShape().getWidth() / 2, mapPos.y
				- shape.getShape().getHeight() / 2);
		select(shape);
		DynamicPolygonObject copy = new DynamicPolygonObject(shape.getType(),
				null, null, -1);
		copy.setPolygonVertices(shape.getPolygonVertices());
		data.setPlacingObject(copy);
	}

	@Override
	public boolean isAnythingAt(int guiX, int guiY) {
		return (isObjectAt(guiX, guiY) || isBaseAt(guiX, guiY) || isSpawnPointAt(
				guiX, guiY));
	}

	@Override
	public void deleteSelected() {
		for (EditorPlaceable element : selectedElements) {
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
		clearSelection();
	}

	@Override
	public void editSelectedShape() {
		if (selectedElements.isEmpty())
			return;
		EditorPlaceable element = selectedElements.get(0);
		if (element instanceof DynamicPolygonObject) {
			DynamicPolygonObject object = (DynamicPolygonObject) element;
			EditablePolygon poly = EditablePolygon.fromShape(object.getShape());
			data.setEditShape(poly);
			data.clearTempLines();
			data.setEditObject(object);
			setInteractType(InteractType.EDIT_SHAPE);
		}
	}

	@Override
	public void importShape(File file) {
		try {
			Vector2f[] verts = ShapeParser.readShape(file.toURI().toURL());
			GameObject o;
			try {
				o = ObjectCreator.createObject(
						ObjectType.DYNAMIC_POLYGON_BLOCK, null, null);
				DynamicPolygonObject poly = (DynamicPolygonObject) o;
				poly.setPolygonVertices(verts);
				data.setPlacingObject(poly);
				setInteractType(InteractType.PLACE_SHAPE);
			} catch (FactoryException | ShapeVerticesNotApplicableException e) {
				JOptionPane.showMessageDialog(window, "Error creating object.");
			}

		} catch (FileCorruptException | IOException e) {
			JOptionPane.showMessageDialog(
					window,
					"An error occured while reading file "
							+ file.getAbsolutePath() + ": " + e.getMessage());
		}
	}

	@Override
	public Point getMouseLocation() {
		return new Point(input.getMouseX(), input.getMouseY());
	}

	@Override
	public void setZoom(float factor) {
		if (factor < 0.1f)
			return;
		this.zoom = factor;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public void rotateSelectedShapes(float degree) {
		for (EditorPlaceable element : selectedElements) {
			if (element instanceof DynamicPolygonObject) {
				DynamicPolygonObject object = (DynamicPolygonObject) element;
				EditablePolygon poly = EditablePolygon.fromShape(object
						.getShape());
				poly.rotate(degree);
				object.setPolygonVertices(poly.getVector2dfs());
			}
		}
	}

	@Override
	public void mirrorSelectedElements(int axis) {
		float minX = data.getWidth();
		float maxX = 0;
		float minY = data.getHeight();
		float maxY = 0;
		for (EditorPlaceable element : selectedElements) {
			minX = Math.min(element.getXPosition(), minX);
			minY = Math.min(element.getYPosition(), minY);
			maxX = Math.max(element.getXPosition() + element.getWidth(), maxX);
			maxY = Math.max(element.getYPosition() + element.getHeight(), maxY);
		}
		float centerX = (maxX + minX) /2;
		float centerY = (maxY + minY) / 2;
		for (EditorPlaceable element : selectedElements) {
			if (axis == EditablePolygon.Y_AXIS) {
				float dx = centerX - element.getXPosition();
				element.setXPosition(element.getXPosition() + 2*dx
						- element.getWidth());
			}
			if (axis == EditablePolygon.X_AXIS) {
				float dy = centerY - element.getYPosition();
				element.setYPosition(element.getYPosition() + 2*dy
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

	@Override
	public void copySelectedElements() {
		List<EditorPlaceable> newObjects = new LinkedList<EditorPlaceable>();
		int offset = 50;
		for (EditorPlaceable element : selectedElements) {
			if (element instanceof GameObject) {
				GameObject o = (GameObject) element;
				GameObject newObject;
				try {
					newObject = ObjectCreator.createObject(o.getType(), null,
							null);
				} catch (FactoryException | ShapeVerticesNotApplicableException e) {
					return;
				}
				newObject.setId(nextId++);
				newObject.setVisible(o.getVisibility());
				newObject.setPosition(o.getXPosition() + offset,
						o.getYPosition() + offset);
				newObject.setzLayer(o.getzLayer());
				newObject.setRefName("");
				if (o instanceof DynamicPolygonObject) {
					((DynamicPolygonObject) newObject)
							.setColor(((DynamicPolygonObject) o).getColor());
					((DynamicPolygonObject) newObject)
							.setPolygonVertices(((DynamicPolygonObject) o)
									.getPolygonVertices());
				}
				data.addGameObject(newObject);
				newObjects.add(newObject);
			} else if (element instanceof NodeData) {
				NodeData node = (NodeData) element;
				NodeData newNode = new NodeData(node.getXPosition() + offset,
						node.getYPosition() + offset, nextId++,
						new LinkedList<NodeData>(), BaseType.NEUTRAL, "");
				newNode.setWidth(node.getWidth());
				newNode.setHeight(node.getHeight());
				newNode.setResourceMultiplicator(node
						.getResourceMultiplicator());
				newNode.setRefName("");
				newNode.setIsMainNode(node.isMainNode());
				data.addBase(newNode);
				newObjects.add(newNode);
			} else if (element instanceof SpawnPosition) {
				SpawnPosition spawn = (SpawnPosition) element;
				SpawnPosition newSpawn = new SpawnPosition(new Rectangle(spawn
						.getArea().getX() + offset, spawn.getArea().getY()
						+ offset, spawn.getArea().getWidth(), spawn.getArea()
						.getHeight()), spawn.getTeaming());
				newSpawn.setRefName("");
				data.addSpawnPoint(newSpawn);
				newObjects.add(newSpawn);
			}
		}
		clearSelection();
		selectedElements.addAll(newObjects);
	}

	@Override
	public boolean selectAt(int guiX, int guiY) {
		clearSelection();
		GameObject o = getObjectAt(guiX, guiY);
		if (o != null) {
			selectedElements.add(o);
			return true;
		} else {
			NodeData node = getBaseAt(guiX, guiY);
			if (node != null) {
				selectedElements.add(node);
				return true;
			} else {
				SpawnPosition spawn = getSpawnPointAt(guiX, guiY);
				if (spawn != null) {
					selectedElements.add(spawn);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void dragSelected(int xDiff, int yDiff) {
		for (EditorPlaceable obj : selectedElements) {
			float oldX = obj.getXPosition();
			float oldY = obj.getYPosition();
			obj.setPosition(oldX + xDiff / zoom, oldY + yDiff / zoom);
		}
	}

	@Override
	public boolean selectIn(Rectangle rect, boolean add) {
		if (!add)
			clearSelection();
		Vector2f topLeft = computeGuiPointToGameCoordinate(new Vector2f(
				rect.getX(), rect.getY()));
		Vector2f bottomRight = computeGuiPointToGameCoordinate(new Vector2f(
				rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));
		Rectangle searchRect = new Rectangle(topLeft.x, topLeft.y,
				bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
		for (GameObject o : data.getGameObjects()) {
			if (Geometry.shapeCollides(o.getShape(), searchRect)) {
				if (!selectedElements.contains(o))
					selectedElements.add(o);
			}
		}
		for (NodeData node : data.getBases()) {
			Rectangle nodeRect = new Rectangle(node.getXPosition(),
					node.getYPosition(), node.getWidth(), node.getHeight());
			if (Geometry.shapeCollides(nodeRect, searchRect)) {
				if (!selectedElements.contains(node))
					selectedElements.add(node);
			}
		}
		for (SpawnPosition spawn : data.getSpawnPoints()) {
			if (Geometry.shapeCollides(spawn.getArea(), searchRect)) {
				if (!selectedElements.contains(spawn))
					selectedElements.add(spawn);
			}
		}
		return !selectedElements.isEmpty();
	}

	@Override
	public void clearSelection() {
		selectedElements.clear();
	}

	@Override
	public List<EditorPlaceable> getSelectedElements() {
		return new LinkedList<EditorPlaceable>(selectedElements);
	}

	@Override
	public void setDragRect(Rectangle rect) {
		dragRect = rect;
	}

	@Override
	public Rectangle getDragRect() {
		return dragRect;
	}

	@Override
	public void toggleSelectionAt(int guiX, int guiY) {
		EditorPlaceable element = null;
		GameObject o = getObjectAt(guiX, guiY);
		if (o != null) {
			element = o;
		} else {
			NodeData node = getBaseAt(guiX, guiY);
			if (node != null) {
				element = node;
			} else {
				SpawnPosition spawn = getSpawnPointAt(guiX, guiY);
				if (spawn != null) {
					element = spawn;
				}
			}
		}
		if (element != null) {
			if (selectedElements.contains(element)) {
				selectedElements.remove(element);
			} else {
				selectedElements.add(element);
			}
		}
	}

	@Override
	public boolean isSelected(int guiX, int guiY) {
		return selectedElements.contains(getObjectAt(guiX, guiY))
				|| selectedElements.contains(getSpawnPointAt(guiX, guiY))
				|| selectedElements.contains(getBaseAt(guiX, guiY));
	}
}