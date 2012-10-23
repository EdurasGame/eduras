package de.illonis.eduras.interfaces;

import de.illonis.eduras.MoveableGameObject.Direction;

public interface Controllable {

	public void startMoving(Direction direction);

	public void stopMoving(Direction direction);

	public void stopMoving();

}
