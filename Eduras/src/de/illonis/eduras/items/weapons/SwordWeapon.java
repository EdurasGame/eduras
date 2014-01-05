package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

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
	 * @param id
	 *            The object id this sword is assigned.
	 */
	public SwordWeapon(GameInformation gi, int id) {
		super(ObjectType.ITEM_WEAPON_SWORD, gi, id);
		setName("Sword");
		setShape(new Circle(S.go_swordweapon_shape_radius));
		defaultCooldown = S.go_swordweapon_cooldown;
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.SWORDMISSILE, info);
	}
}
