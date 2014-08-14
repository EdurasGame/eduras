package de.illonis.eduras.mapeditor;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.InputAdapter;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

public class MapInputHandler extends InputAdapter {

	private final StatusListener status;
	private final MapInteractor interactor;

	public MapInputHandler(MapInteractor interactor, StatusListener status) {
		this.status = status;
		this.interactor = interactor;
	}
	
	private void updateCoordinateStatus() {
		
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		Vector2f mapCoord = interactor
				.computeGuiPointToGameCoordinate(new Vector2f(newx, newy));
		status.setStatus(mapCoord.x + ", " + mapCoord.y);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		interactor.scroll(oldx - newx, oldy - newy);
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
