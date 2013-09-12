package de.illonis.eduras.items.weapons;

import java.security.InvalidParameterException;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * An weapon that needs ammunition. Ammunition is stored directly in
 * {@link AmmoWeapon} object.
 * 
 * @author illonis
 * 
 */
public abstract class AmmoWeapon extends Weapon {

	private int ammunition;

	/**
	 * Creates a new weapon with ammunition.
	 * 
	 * @param type
	 *            weapon type.
	 * @param gi
	 *            game data.
	 * @param id
	 *            weapon id.
	 */
	public AmmoWeapon(ObjectType type, GameInformation gi, int id) {
		super(type, gi, id);
	}

	/**
	 * Returns ammo of weapon.
	 * 
	 * @return ammo of weapon.
	 */
	public int getAmmo() {
		return ammunition;
	}

	/**
	 * Sets ammunition of weapon to given value.
	 * 
	 * @param ammunition
	 *            new value.
	 */
	public void setAmmo(int ammunition) {
		this.ammunition = ammunition;
	}

	/**
	 * Adds given quantity to ammunition.
	 * 
	 * @param ammo
	 *            quantity to add. Must be positive.
	 */
	public void addAmmo(int ammo) {
		if (ammo <= 0)
			throw new InvalidParameterException(
					"Quantity adding to ammunition must be positive");
		ammunition += ammo;
	}

}
