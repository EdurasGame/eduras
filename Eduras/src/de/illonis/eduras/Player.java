package de.illonis.eduras;

import de.illonis.eduras.interfaces.Controllable;

public class Player extends MoveableGameObject implements Controllable {

	public Player(GameInformation game) {
		super(game);
	}

	@Override
	public void startMoving(Direction direction) {
	}

	@Override
	public void stopMoving(Direction direction) {
	}

	@Override
	public void stopMoving() {
	}

}
