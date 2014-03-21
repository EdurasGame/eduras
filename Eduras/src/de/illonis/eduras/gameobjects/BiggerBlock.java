package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.settings.S;

/**
 * creates a bigger block than the normal bigblock
 * 
 * @author Jan Reese
 * 
 */
public class BiggerBlock extends Block {

	private static final int WIDTH = S.go_bigger_block_width;
	private static final int HEIGHT = S.go_bigger_block_height;

	/**
	 * creates a bigger block at a given position.
	 * 
	 * @param game
	 *            the game information.
	 * @param timingSource
	 *            the timing source
	 * @param xPos
	 *            x-position.
	 * @param yPos
	 *            y-position.
	 * @param id
	 *            object id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BiggerBlock(GameInformation game, TimingSource timingSource,
			double xPos, double yPos, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, xPos, yPos, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGGERBLOCK);
	}

	/**
	 * creates a bigger block
	 * 
	 * @param game
	 *            the gameinformation.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BiggerBlock(GameInformation game, TimingSource timingSource, int id)
			throws ShapeVerticesNotApplicableException {
		this(game, timingSource, 0, 0, id);
	}

}
