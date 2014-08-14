package de.illonis.eduras.mapeditor;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

public interface MapInteractor {

	void scroll(int x, int y);

	void startScrolling(Direction dir);

	void stopScrolling(Direction dir);

	Vector2f computeGuiPointToGameCoordinate(Vector2f guiPoint);

	GameCamera getViewPort();

	float getZoom();

}
