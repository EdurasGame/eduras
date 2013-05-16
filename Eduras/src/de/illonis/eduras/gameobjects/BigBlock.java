package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;

/**
 * This is a special block... a big one.
 * 
 * @author renmai
 * 
 */
public class BigBlock extends Block {

	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;

	/**
	 * Creates a BigBlock at the given position and id in the context of the
	 * given game information.
	 * 
	 * @param game
	 * @param xPos
	 * @param yPos
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BigBlock(GameInformation game, double xPos, double yPos, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, xPos, yPos, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGBLOCK);
	}

	/**
	 * Creates a BigBlock with the given id in the context of a gameinformation.
	 * 
	 * @param game
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BigBlock(GameInformation game, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGBLOCK);
	}

}
