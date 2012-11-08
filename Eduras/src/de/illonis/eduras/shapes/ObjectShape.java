/**
 * 
 */
package de.illonis.eduras.shapes;

import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

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
	public abstract Vector2D checkCollision(GameInformation game,
			GameObject thisObject, Vector2D target);

	/**
	 * Checks if the shape related to a specific object is intersected by
	 * another moving object, which is represented by lines.
	 * 
	 * @param lines
	 *            The lines representing the moving object.
	 * @param thisObject
	 *            The object to which the shape belongs.
	 * @return Returns a linked list of position vectors to the points of
	 *         collisions. The list will be empty if there is no collision.
	 */
	public abstract LinkedList<Vector2D> isIntersected(LinkedList<Line> lines,
			GameObject thisObject);

	/**
	 * Returns the line segments which represents the borders of the shape.
	 * 
	 * @return The line segments representing the borders of the shape.
	 */
	public abstract LinkedList<Line> getBorderLines();

}
