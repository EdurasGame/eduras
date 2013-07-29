package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;

/**
 * An active game object is a gameobject that has Artificial Intelligence.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ActiveGameObject extends GameObject {

	/**
	 * Create an active game object.
	 * 
	 * @param game
	 *            The gameinformation.
	 * @param id
	 *            The new objects id.
	 */
	public ActiveGameObject(GameInformation game, int id) {
		super(game, id);
	}

	@Override
	public abstract void onCollision(GameObject collidingObject);

	/**
	 * Returns a new instance of the object's AI.
	 * 
	 * @return The object's AI.
	 */
	public abstract ArtificialIntelligence getAI();

}
