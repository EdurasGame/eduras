package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;

/**
 * The spawn weapon is a short-range weapon with small cooldown and damage.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SwordWeapon extends Weapon {

	/**
	 * Create a new SwordWeapon.
	 * 
	 * @param gi
	 *            The game info context.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The object id this sword is assigned.
	 */
	public SwordWeapon(GameInformation gi, TimingSource timingSource, int id) {
		super(ObjectType.ITEM_WEAPON_SWORD, gi, timingSource, id);
		setName("Sword");
		setShape(new Circle(S.Server.go_swordweapon_shape_radius,
				S.Server.go_swordweapon_shape_radius,
				S.Server.go_swordweapon_shape_radius));
		defaultCooldown = S.Server.go_swordweapon_cooldown;
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.SWORDMISSILE, info);
	}
}
