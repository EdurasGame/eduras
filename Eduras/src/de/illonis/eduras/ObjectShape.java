/**
 * 
 */
package de.illonis.eduras;

import java.awt.geom.Point2D;

/**
 * Super class of any shape a object in the game can have.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ObjectShape {

	/**
	 * Checks if there is will be a collision if the object tries to move from
	 * its current position to the target position.
	 * 
	 * @param game
	 *            The current state of the game.
	 * @param thisObject
	 *            The object which wants to move.
	 * @param target
	 *            The target position
	 * @return Returns the position of the object after the move.
	 */
	public abstract Point2D.Double checkCollision(Game game,
			GameObject thisObject, Point2D.Double target);

}
