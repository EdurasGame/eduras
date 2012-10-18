package de.illonis.eduras;

import de.illonis.eduras.interfaces.Moveable;

public abstract class MoveableGameObject extends GameObject implements Moveable {

	public static enum Direction {
		LEFT, RIGHT, UP, DOWN
	}

	int speed = 0;

	public final static boolean isHorizontal(Direction direction) {
		return (direction == Direction.LEFT || direction == Direction.RIGHT);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

}
