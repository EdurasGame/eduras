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
	 * Create a new player that belongs to the given game and has the given
	 * owner.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 * @param ownerId
	 *            The id of the owner.
	 */
	public Player(GameInformation game, int ownerId) {
		super(game);
		setOwner(ownerId);
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
