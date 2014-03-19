package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.settings.S;

/**
 * This is a special block... a big one.
 * 
 * @author renmai
 * 
 */
public class BigBlock extends Block {

	private static final int WIDTH = S.go_big_block_width;
	private static final int HEIGHT = S.go_big_block_height;

	/**
	 * Creates a BigBlock at the given position and id in the context of the
	 * given game information.
	 * 
	 * @param game
	 * @param timingSource
	 *            the timing source.
	 * @param xPos
	 * @param yPos
	 * @param id
	 *            object id.
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BigBlock(GameInformation game, TimingSource timingSource,
			double xPos, double yPos, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, xPos, yPos, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGBLOCK);
	}

	/**
	 * Creates a BigBlock with the given id in the context of a gameinformation.
	 * 
	 * @param game
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BigBlock(GameInformation game, TimingSource timingSource, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGBLOCK);
	}

}
