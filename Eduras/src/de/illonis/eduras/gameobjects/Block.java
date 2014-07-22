package de.illonis.eduras.gameobjects;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;

/**
 * This represents a block.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class Block extends GameObject {

	protected float height;
	protected float width;

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
	public Block(GameInformation game, TimingSource timingSource, float xPos,
			float yPos, float width, float height, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, id);

		this.width = width;
		this.height = height;
		setShape(new Rectangle(0, 0, width, height));
		setPosition(xPos, yPos);
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
	public Block(GameInformation game, TimingSource timingSource, float width,
			float height, int id) throws ShapeVerticesNotApplicableException {
		this(game, timingSource, 0f, 0f, width, height, id);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// do nothing
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		return true;
	}
}
