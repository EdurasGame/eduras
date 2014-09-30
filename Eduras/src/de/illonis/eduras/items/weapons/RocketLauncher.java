package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ShapeFactory;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

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
	 * @param timingSource
	 *            the timingsource.
	 * @param id
	 *            object id.
	 */
	public RocketLauncher(GameInformation infos, TimingSource timingSource,
			int id) {
		super(ObjectType.ROCKETLAUNCHER, infos, timingSource, id);
		setName("Rocket Launcher");
		setSortOrder(4);
		setShape(ShapeFactory.createShape(ShapeType.ROCKET));
		defaultCooldown = S.Server.go_rocketlauncher_cooldown;
		setAmmunitionLimited(S.Server.go_rocketlauncher_fillamount,
				S.Server.go_rocketlauncher_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.ROCKET_MISSILE, info);
	}
}
