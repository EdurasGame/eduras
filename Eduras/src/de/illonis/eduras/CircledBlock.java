/**
 * 
 */
package de.illonis.eduras;

import de.illonis.eduras.shapes.Circle;

/**
 * 
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CircledBlock extends GameObject {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.GameObject#onCollision(de.illonis.eduras.GameObject)
	 */
	@Override
	public void onCollision(GameObject collidingObject) {
		// do nothing

	}

	/**
	 * Creates a new CircledBlock at the position (posX,posY) and with the
	 * radius 'radius'.
	 * 
	 * @param radius
	 *            The radius of the circled block.
	 * @param posX
	 *            The x-component of the position.
	 * @param posY
	 *            The y-component of the position.
	 */
	public CircledBlock(GameInformation game, double radius, double posX,
			double posY, int id) {

		super(game, id);

		setPosition(posX, posY);
		setShape(new Circle(radius));
	}
}
