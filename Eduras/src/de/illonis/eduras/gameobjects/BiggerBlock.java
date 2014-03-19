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
	 * creates the bigger block
	 * 
	 * @param game
	 * @param timingSource
	 * @param xPos
	 * @param yPos
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BiggerBlock(GameInformation game, TimingSource timingSource,
			double xPos, double yPos, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, xPos, yPos, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGGERBLOCK);
	}

	/**
	 * creates the bigger block
	 * 
	 * @param game
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BiggerBlock(GameInformation game, TimingSource timingSource, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGGERBLOCK);
	}

}
