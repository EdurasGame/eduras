package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SimpleMissile extends Missile {

	/**
	 * Creates a new simplemissile
	 * 
	 * @param game
	 *            The game context
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id of the new missile
	 */
	public SimpleMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setDamage(S.Server.go_simplemissile_damage);
		setDamageRadius(S.Server.go_simplemissile_damage_radius);
		setObjectType(ObjectType.SIMPLEMISSILE);
		setSpeed(S.Server.go_simplemissile_speed);
		setMaxRange(S.Server.go_simplemissile_maxrange);
	}

}
