package de.illonis.eduras.mapeditor;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.mapeditor.gui.dialog.PropertiesDialog;

public class MapPanelLogic implements MapInteractor {

	private final GameCamera viewPort;
	private float zoom = 1f;
	private Vector2f scrollVector;
	private final static float SCROLL_SPEED = 5;
	private final MapData data = MapData.getInstance();
	private ObjectType currentSpawnType = ObjectType.NO_OBJECT;
	private final EditorWindow window;

	public MapPanelLogic(EditorWindow window) {
		this.window = window;
		viewPort = new GameCamera();
		viewPort.setSize(800, 600);
		scrollVector = new Vector2f();
	}

	/**
	 * Computes a point that is relative to gui into game coordinates.
	 * 
	 * @param v
	 *            point to convert.
	 * @return game-coordinate point.
	 */
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

	public void update(int delta) {
		float x = viewPort.getX();
		float y = viewPort.getY();
		viewPort.setLocation(x + scrollVector.x, scrollVector.y + y);
	}

	@Override
	public void dragObjectAt(int guiX, int guiY, int xDiff, int yDiff) {
		GameObject o = getObjectAt(guiX, guiY);
		if (o != null) {
			o.modifyXPosition(xDiff);
			o.modifyYPosition(yDiff);
		}
	}

	@Override
	public void showPropertiesOfObjectAt(int guiX, int guiY) {
		GameObject o = getObjectAt(guiX, guiY);
		if (o != null) {
			PropertiesDialog dialog = new PropertiesDialog(window, o);
			dialog.setVisible(true);
			System.out.println("properties for " + o.getType());
		}
	}

	@Override
	public boolean isObjectAt(int guiX, int guiY) {
		return (getObjectAt(guiX, guiY) != null);
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
			// TODO: create object
		}
	}
}