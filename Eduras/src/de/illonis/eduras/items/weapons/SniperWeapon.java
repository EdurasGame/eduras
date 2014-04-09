package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;

/**
 * A sniper weapon that shoots fast missiles with higher damage and cooldown.
 * 
 * @author Jan Reese
 * 
 */
public class SniperWeapon extends Weapon {
	/**
	 * 
	 * @param gi
	 *            game info
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            weapon id
	 */
	public SniperWeapon(GameInformation gi, TimingSource timingSource, int id) {
		super(ObjectType.ITEM_WEAPON_SNIPER, gi, timingSource, id);
		setName("Sniper");
		setShape(new Circle((float) S.go_sniperweapon_shape_radius,
				(float) S.go_sniperweapon_shape_radius,
				(float) S.go_sniperweapon_shape_radius));
		defaultCooldown = S.go_sniperweapon_cooldown;
		setAmmunitionLimited(S.go_sniperweapon_fillamount,
				S.go_sniperweapon_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.SNIPERMISSILE, info);
	}
}
