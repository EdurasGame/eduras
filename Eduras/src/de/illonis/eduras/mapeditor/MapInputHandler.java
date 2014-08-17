package de.illonis.eduras.mapeditor;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.InputAdapter;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

/**
 * Handles user input in map editor.
 * 
 * @author illonis
 * 
 */
public class MapInputHandler extends InputAdapter {

	enum InteractMode {
		SCROLL, DRAG, NONE;
	}

	private InteractMode mode;

	private final StatusListener status;
	private final MapInteractor interactor;

	MapInputHandler(MapInteractor interactor, StatusListener status) {
		this.status = status;
		this.interactor = interactor;
		mode = InteractMode.NONE;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_RIGHT_BUTTON) {
			mode = InteractMode.SCROLL;
		} else if (button == Input.MOUSE_LEFT_BUTTON) {
			if (interactor.isAnythingAt(x, y)) {
				mode = InteractMode.DRAG;
			}
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (button == Input.MOUSE_RIGHT_BUTTON) {
			interactor.showPropertiesOfThingAt(x, y);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (mode == InteractMode.NONE) {
			switch (interactor.getInteractType()) {
			case PLACE_OBJECT:
				interactor.spawnAt(x, y);
				break;
			case PLACE_BASE:
				interactor.createBaseAt(x, y);
				break;
			case PLACE_SPAWN:
				interactor.createSpawnPointAt(x, y);
				break;
			case PLACE_SHAPE:
				interactor.placeShapeAt(x, y);
				break;
			default:
				break;
			}
		}
		mode = InteractMode.NONE;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		updateCoordinateStatus(newx, newy);
	}

	private void updateCoordinateStatus(int newx, int newy) {
		Vector2f mapCoord = interactor
				.computeGuiPointToGameCoordinate(new Vector2f(newx, newy));
		status.setStatus(mapCoord.x + ", " + mapCoord.y);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (mode == InteractMode.SCROLL)
			interactor.scroll(oldx - newx, oldy - newy);
		else if (mode == InteractMode.DRAG) {
			interactor.dragThingAt(oldx, oldy, newx - oldx, newy - oldy);
		}
		updateCoordinateStatus(newx, newy);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_A:
			interactor.startScrolling(Direction.LEFT);
			break;
		case Input.KEY_D:
			interactor.startScrolling(Direction.RIGHT);
			break;
		case Input.KEY_W:
			interactor.startScrolling(Direction.TOP);
			break;
		case Input.KEY_S:
			interactor.startScrolling(Direction.BOTTOM);
			break;
		case Input.KEY_X:
			interactor.deleteAtMouse();
			break;
		case Input.KEY_V:
			interactor.editShapeAtMouse();
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_A:
			interactor.stopScrolling(Direction.LEFT);
			break;
		case Input.KEY_D:
			interactor.stopScrolling(Direction.RIGHT);
			break;
		case Input.KEY_W:
			interactor.stopScrolling(Direction.TOP);
			break;
		case Input.KEY_S:
			interactor.stopScrolling(Direction.BOTTOM);
			break;
		default:
			break;
		}
	}
}
