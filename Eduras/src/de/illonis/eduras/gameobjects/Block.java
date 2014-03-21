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

	protected double height;
	protected double width;

	/**
	 * Creates a new block at position xPos and yPos.
	 * 
	 * @param game
	 *            The game information.
	 * @param timingSource
	 *            the timing source.
	 * @param xPos
	 *            The x position of the block.
	 * @param yPos
	 *            The y position of the block.
	 * @param width
	 *            the width of the block
	 * @param height
	 *            the height of the block
	 * @param id
	 *            the id of the object.
	 * @throws ShapeVerticesNotApplicableException
	 *             Thrown if the given position values do not apply.
	 */
	public Block(GameInformation game, TimingSource timingSource, double xPos,
			double yPos, double width, double height, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, id);

		this.width = width;
		this.height = height;

		setPosition(xPos, yPos);
		setShape(new Rectangle(new Vector2D(), new Vector2D(width, height)));
	}

	/**
	 * Creates a new block.
	 * 
	 * @param game
	 *            The game information.
	 * @param timingSource
	 *            the timing source.
	 * @param width
	 *            the width of the block.
	 * @param height
	 *            the height of the block.
	 * @param id
	 *            the object id.
	 * @throws ShapeVerticesNotApplicableException
	 *             Thrown if the given position values do not apply.
	 */
	public Block(GameInformation game, TimingSource timingSource, double width,
			double height, int id) throws ShapeVerticesNotApplicableException {
		this(game, timingSource, 0, 0, width, height, id);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// do nothing
	}
}
