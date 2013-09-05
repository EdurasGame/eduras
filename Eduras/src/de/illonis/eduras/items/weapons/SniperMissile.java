/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.ArtificialIntelligence;
import de.illonis.eduras.shapes.Circle;

/**
 * 
 * @author Jan Reese
 * 
 */
public class SniperMissile extends Missile {
	/**
	 * creates a new SniperMissle
	 * 
	 * @param game
	 *            game context
	 * @param id
	 *            id of the new missile
	 */
	public SniperMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(16);
		setDamageRadius(1.5);
		setObjectType(ObjectType.SNIPERMISSILE);
		setShape(new Circle(1.5));
		setSpeed(500);
	}

	@Override
	public ArtificialIntelligence getAI() {
		// TODO Auto-generated method stub
		return null;
	}

}
