package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ShapeFactory;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

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
		setShape(ShapeFactory.createShape(ShapeType.WEAPON_SNIPER));
		defaultCooldown = S.Server.go_sniperweapon_cooldown;
		setAmmunitionLimited(S.Server.go_sniperweapon_fillamount,
				S.Server.go_sniperweapon_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.SNIPERMISSILE, info);
	}
}
