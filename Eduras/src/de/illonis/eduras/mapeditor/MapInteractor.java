package de.illonis.eduras.mapeditor;

import java.io.File;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

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
		PLACE_BASE, PLACE_SPAWN, ROTATE_SHAPE, PLACE_OBJECT, DEFAULT, PLACE_SHAPE, EDIT_SHAPE;
	}

	void setDragRect(Rectangle rect);

	Rectangle getDragRect();

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
	
	boolean undo();
	
	boolean redo();

	void setZoom(float factor);

	void showPropertiesOfSelected();

	boolean isObjectAt(int guiX, int guiY);

	void setSpawnType(ObjectType selectedValue);

	void spawnAt(int guiX, int guiY);

	void createBaseAt(int guiX, int guiY);

	void createSpawnPointAt(int guiX, int guiY);

	void placeShapeAt(int guiX, int guiY);

	boolean isAnythingAt(int guiX, int guiY);

	boolean isBaseAt(int guiX, int guiY);

	boolean isSpawnPointAt(int guiX, int guiY);

	void deleteSelected();

	void editSelectedShape();

	void importShape(File file);

	Point getMouseLocation();

	Input getInput();

	void rotateSelectedShapes(float degree);

	void mirrorSelectedElements(int axis);

	void copySelectedElements();

	boolean selectAt(int x, int y);

	boolean selectIn(Rectangle rect, boolean add);

	void dragSelected(int xDiff, int yDiff);

	List<EditorPlaceable> getSelectedElements();

	void clearSelection();

	void toggleSelectionAt(int x, int y);

	boolean isSelected(int x, int y);
	
	void onMapLoaded();

}
