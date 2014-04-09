package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;

/**
 * A splash weapon shoots a slow missile that explodes on collision and spreads
 * into multiple other missiles.
 * 
 * @author illonis
 * 
 */
public class SplashWeapon extends Weapon {

	/**
	 * Creates a new splashweapon.
	 * 
	 * @param infos
	 *            game info.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public SplashWeapon(GameInformation infos, TimingSource timingSource, int id) {
		super(ObjectType.ITEM_WEAPON_SPLASH, infos, timingSource, id);
		setName("SplashWeapon");
		setShape(new Circle((float) S.go_splashweapon_shape_radius,
				(float) S.go_splashweapon_shape_radius,
				(float) S.go_splashweapon_shape_radius));
		defaultCooldown = S.go_splashweapon_cooldown;
		setAmmunitionLimited(S.go_splashweapon_fillamount,
				S.go_splashweapon_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.MISSILE_SPLASH, info);
	}
}
