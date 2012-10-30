package de.illonis.eduras;

import de.illonis.eduras.interfaces.Controllable;

/**
 * This class represents a player.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Player extends MoveableGameObject implements Controllable {

	/**
	 * Create a new player that belongs to the given game.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 */
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
