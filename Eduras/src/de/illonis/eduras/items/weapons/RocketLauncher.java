package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;
import de.illonis.eduras.shapes.ShapeFactory;

/**
 * A rocket launcher launches slow missiles that explode on collision.
 * 
 * @author illonis
 * 
 */
public class RocketLauncher extends Weapon {

	/**
	 * Creates a new rocket launcher.
	 * 
	 * @param infos
	 *            game info.
	 * @param id
	 *            object id.
	 */
	public RocketLauncher(GameInformation infos, int id) {
		super(ObjectType.ROCKETLAUNCHER, infos, id);
		setName("Rocket Launcher");

		// TODO: make a shape
		setShape(ShapeFactory.createShape(ShapeType.ROCKET));
		defaultCooldown = S.go_rocketlauncher_cooldown;
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.ROCKET_MISSILE, info);
	}
}
