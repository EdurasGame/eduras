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

	private static final float WIDTH = S.go_big_block_width;
	private static final float HEIGHT = S.go_big_block_height;

	/**
	 * Creates a BigBlock at the given position and id in the context of the
	 * given game information.
	 * 
	 * @param game
	 *            the game info.
	 * @param timingSource
	 *            the timing source.
	 * @param xPos
	 *            x-position
	 * @param yPos
	 *            y-position
	 * @param id
	 *            object id.
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BigBlock(GameInformation game, TimingSource timingSource,
			float xPos, float yPos, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, timingSource, xPos, yPos, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGBLOCK);
	}

	/**
	 * Creates a BigBlock with the given id in the context of a gameinformation.
	 * 
	 * @param game
	 *            the game info.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            the object id.
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BigBlock(GameInformation game, TimingSource timingSource, int id)
			throws ShapeVerticesNotApplicableException {
		this(game, timingSource, 0, 0, id);
	}

}
