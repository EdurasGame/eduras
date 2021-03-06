package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ShapeFactory;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SimpleWeapon extends Weapon {

	/**
	 * Creates a new instance of the example weapon.
	 * 
	 * @param gi
	 *            game information
	 * @param timingSource
	 *            the timingsource.
	 * @param id
	 *            id of weapon
	 */
	public SimpleWeapon(GameInformation gi, TimingSource timingSource, int id) {
		super(ObjectType.ITEM_WEAPON_SIMPLE, gi, timingSource, id);
		setName("SimpleWeapon");
		setSortOrder(2);
		setShape(ShapeFactory.createShape(ShapeType.WEAPON_1));
		defaultCooldown = S.Server.go_simpleweapon_cooldown;
		setAmmunitionLimited(S.Server.go_simpleweapon_fillamount,
				S.Server.go_simpleweapon_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.SIMPLEMISSILE, info);
	}
}
