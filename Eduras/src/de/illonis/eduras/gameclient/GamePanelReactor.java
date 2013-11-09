package de.illonis.eduras.gameclient;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.math.Vector2D;

public interface GamePanelReactor {

	void onItemUse(int slotId, Vector2D target);

	void onStartMovement(Direction direction);

	void onStopMovement(Direction direction);

	void onGameQuit();

	void onModeSwitch();
}
