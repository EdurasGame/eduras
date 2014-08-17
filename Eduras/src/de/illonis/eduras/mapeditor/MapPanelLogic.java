package de.illonis.eduras.mapeditor;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.FactoryException;
import de.illonis.eduras.ObjectCreator;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.mapeditor.gui.dialog.PropertiesDialog;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;

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

	MapPanelLogic(EditorWindow window) {
		this.window = window;
		interactType = InteractType.DEFAULT;
		viewPort = new GameCamera();
		viewPort.setSize(800, 600);
		scrollVector = new Vector2f();
	}

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
	public void dragThingAt(int guiX, int guiY, int xDiff, int yDiff) {
		GameObject o = getObjectAt(guiX, guiY);
		if (o != null) {
			o.modifyXPosition(xDiff);
			o.modifyYPosition(yDiff);
		} else {
			NodeData node = getBaseAt(guiX, guiY);
			if (node != null) {
				node.setX(node.getX() + xDiff);
				node.setY(node.getY() + yDiff);
			} else {
				SpawnPosition spawn = getSpawnPointAt(guiX, guiY);
				if (spawn != null) {
					spawn.getArea().setLocation(spawn.getArea().getX() + xDiff,
							spawn.getArea().getY() + yDiff);
				}
			}
		}
	}

	@Override
	public void showPropertiesOfThingAt(int guiX, int guiY) {
		GameObject o = getObjectAt(guiX, guiY);
		PropertiesDialog dialog = null;
		if (o != null) {
			dialog = new PropertiesDialog(window, o);
		} else {
			NodeData node = getBaseAt(guiX, guiY);
			if (node != null) {
				dialog = new PropertiesDialog(window, node);
			} else {
				SpawnPosition spawn = getSpawnPointAt(guiX, guiY);
				if (spawn != null) {
					dialog = new PropertiesDialog(window, spawn);
				}
			}
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

	@Override
	public GameObject getObjectAt(int guiX, int guiY) {
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
					float width = o.getShape().getWidth();
					float height = o.getShape().getHeight();
					guiX -= width / 2;
					guiY -= height / 2;
				}
				Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(
						guiX, guiY));
				o.setPosition(mapPos.x, mapPos.y);
				o.setId(nextId++);
				data.addGameObject(o);
			} catch (FactoryException | ShapeVerticesNotApplicableException e) {
				JOptionPane.showMessageDialog(window, "Object "
						+ currentSpawnType + " not supported");
			}
		}
	}

	@Override
	public NodeData getBaseAt(int guiX, int guiY) {
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		for (NodeData o : data.getBases()) {
			Rectangle r = new Rectangle(o.getX(), o.getY(), o.getWidth(),
					o.getHeight());
			if (r.contains(mapPos.x, mapPos.y)) {
				return o;
			}
		}
		return null;
	}

	@Override
	public SpawnPosition getSpawnPointAt(int guiX, int guiY) {
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
		guiX -= width / 2;
		guiY -= height / 2;
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		NodeData node = new NodeData(mapPos.x, mapPos.y, nextId++,
				new LinkedList<Integer>(), BaseType.NEUTRAL);
		data.addBase(node);
	}

	@Override
	public void createSpawnPointAt(int guiX, int guiY) {
		float width = 40;
		float height = 40;
		guiX -= width / 2;
		guiY -= height / 2;
		Vector2f mapPos = computeGuiPointToGameCoordinate(new Vector2f(guiX,
				guiY));
		SpawnPosition spawn = new SpawnPosition(new Rectangle(mapPos.x,
				mapPos.y, 40, 40), SpawnType.ANY);
		data.addSpawnPoint(spawn);
	}

	@Override
	public void setInteractType(InteractType type) {
		interactType = type;
	}

	@Override
	public InteractType getInteractType() {
		return interactType;
	}

	@Override
	public void placeShapeAt(int guiX, int guiY) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isAnythingAt(int guiX, int guiY) {
		return (isObjectAt(guiX, guiY) || isBaseAt(guiX, guiY) || isSpawnPointAt(
				guiX, guiY));
	}

	@Override
	public void deleteAtMouse() {
		int x = input.getMouseX();
		int y = input.getMouseY();
		GameObject o = getObjectAt(x, y);
		if (o != null) {
			data.remove(o);
		} else {
			NodeData node = getBaseAt(x, y);
			if (node != null) {
				data.remove(node);
			} else {
				SpawnPosition spawn = getSpawnPointAt(x, y);
				if (spawn != null) {
					data.remove(spawn);
				}
			}
		}
	}

	@Override
	public void editShapeAtMouse() {
		int x = input.getMouseX();
		int y = input.getMouseY();
		GameObject o = getObjectAt(x, y);
		if (o.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK) {

		}
	}
}