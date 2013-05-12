/**
 * 
 */
package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Rectangle;

/**
 * This represents a block.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class Block extends GameObject {

	double height;
	double width;

	/**
	 * Creates a new block at position xPos and yPos.
	 * 
	 * @param game
	 *            The game information.
	 * @param xPos
	 *            The x position of the block.
	 * @param yPos
	 *            The y position of the block.
	 * @throws ShapeVerticesNotApplicableException
	 *             Thrown if the given position values do not apply.
	 */
	public Block(GameInformation game, double xPos, double yPos, double width,
			double height, int id) throws ShapeVerticesNotApplicableException {
		super(game, id);

		this.width = width;
		this.height = height;

		setPosition(xPos, yPos);
		setShape(new Rectangle(new Vector2D(-width / 2, height / 2),
				new Vector2D(width / 2, -height / 2)));
	}

	/**
	 * Creates a new block.
	 * 
	 * @param game
	 *            The game information.
	 * @throws ShapeVerticesNotApplicableException
	 *             Thrown if the given position values do not apply.
	 */
	public Block(GameInformation game, double width, double height, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, id);

		this.width = width;
		this.height = height;

		setShape(new Rectangle(new Vector2D(-width / 2, height / 2),
				new Vector2D(width / 2, -height / 2)));
	}

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
}
