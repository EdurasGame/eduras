package de.illonis.eduras.gameobjects;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;

/**
 * A circled block.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class CircledBlock extends GameObject {

	@Override
	public void onCollision(GameObject collidingObject) {
		// do nothing
	}

	/**
	 * Creates a new CircledBlock at the position (posX,posY) and with the
	 * radius 'radius'.
	 * 
	 * @param game
	 *            the game information.
	 * @param timingSource
	 *            the timing source.
	 * 
	 * @param radius
	 *            The radius of the circled block.
	 * @param posX
	 *            The x-component of the position.
	 * @param posY
	 *            The y-component of the position.
	 * @param id
	 */
	public CircledBlock(GameInformation game, TimingSource timingSource,
			double radius, double posX, double posY, int id) {
		super(game, timingSource, id);
		setShape(new Circle((float) (posX + radius), (float) (posY + radius),
				(float) radius));

		setPosition((float) posX, (float) posY);
	}
}
