package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ai.AIControllable;

/**
 * An active game object is a gameobject that has Artificial Intelligence.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ActiveGameObject extends GameObject implements
		AIControllable {

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

}
