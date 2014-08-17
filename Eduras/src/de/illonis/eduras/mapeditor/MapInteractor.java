package de.illonis.eduras.mapeditor;

import java.io.File;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;

/**
 * Provides interaction with the edited map.
 * 
 * @author illonis
 * 
 */
public interface MapInteractor {

	/**
	 * Types of interaction
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum InteractType {
		PLACE_BASE, PLACE_SPAWN, PLACE_OBJECT, DEFAULT, PLACE_SHAPE;
	}

	void setInteractType(InteractType type);

	InteractType getInteractType();

	void scroll(int x, int y);

	void startScrolling(Direction dir);

	void stopScrolling(Direction dir);

	/**
	 * Computes a point that is relative to gui into game coordinates.
	 * 
	 * @param guiPoint
	 *            point to convert.
	 * @return game-coordinate point.
	 */
	Vector2f computeGuiPointToGameCoordinate(Vector2f guiPoint);

	GameCamera getViewPort();

	float getZoom();

	void dragThingAt(int guiX, int guiY, int xDiff, int yDiff);

	void showPropertiesOfThingAt(int guiX, int guiY);

	boolean isObjectAt(int guiX, int guiY);

	GameObject getObjectAt(int guiX, int guiY);

	NodeData getBaseAt(int guiX, int guiY);

	SpawnPosition getSpawnPointAt(int guiX, int guiY);

	void setSpawnType(ObjectType selectedValue);

	void spawnAt(int guiX, int guiY);

	void createBaseAt(int guiX, int guiY);

	void createSpawnPointAt(int guiX, int guiY);

	void placeShapeAt(int guiX, int guiY);

	boolean isAnythingAt(int guiX, int guiY);

	boolean isBaseAt(int guiX, int guiY);

	boolean isSpawnPointAt(int guiX, int guiY);

	void deleteAtMouse();

	void editShapeAtMouse();

	void importShape(File file);
	
	Point getMouseLocation();

}
