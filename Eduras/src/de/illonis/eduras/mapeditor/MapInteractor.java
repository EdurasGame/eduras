package de.illonis.eduras.mapeditor;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

public interface MapInteractor {

	void scroll(int x, int y);

	void startScrolling(Direction dir);

	void stopScrolling(Direction dir);

	Vector2f computeGuiPointToGameCoordinate(Vector2f guiPoint);

	GameCamera getViewPort();

	float getZoom();

	void dragObjectAt(int guiX, int guiY, int xDiff, int yDiff);

	void showPropertiesOfObjectAt(int guiX, int guiY);
	
	boolean isObjectAt(int guiX, int guiY);
	
	GameObject getObjectAt(int guiX, int guiY);

	void setSpawnType(ObjectType selectedValue);
	
	void spawnAt(int guiX, int guiY);

}
