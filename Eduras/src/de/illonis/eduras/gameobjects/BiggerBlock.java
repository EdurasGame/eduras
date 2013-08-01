package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;

/**
 * creates a bigger block than the normal bigblock
 * 
 * @author Jan Reese
 * 
 */
public class BiggerBlock extends Block {

	private static final int WIDTH = 40;
	private static final int HEIGHT = 40;

	/**
	 * creates the bigger block
	 * 
	 * @param game
	 * @param xPos
	 * @param yPos
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BiggerBlock(GameInformation game, double xPos, double yPos, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, xPos, yPos, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGGERBLOCK);
	}

	/**
	 * creates the bigger block
	 * 
	 * @param game
	 * @param id
	 * @throws ShapeVerticesNotApplicableException
	 */
	public BiggerBlock(GameInformation game, int id)
			throws ShapeVerticesNotApplicableException {
		super(game, WIDTH, HEIGHT, id);
		setObjectType(ObjectType.BIGGERBLOCK);
	}

}
