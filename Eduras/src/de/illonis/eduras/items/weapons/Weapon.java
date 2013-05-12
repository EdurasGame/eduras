package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.NoAmmunitionException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.Usable;

/**
 * A weapon holds a missile prototype that is used for shooting.
 * 
 * @author illonis
 * 
 */
public abstract class Weapon extends Item implements Lootable, Usable {

	private final int damage = 0;
	private long cooldown = 0;
	protected long defaultCooldown = 0;
	private Missile missile;

	public Weapon(ObjectType type, GameInformation gi, int id) {
		super(type, gi, id);
	}

	/**
	 * Returns missile prototype.
	 * 
	 * @return missile prototype.
	 */
	public Missile getMissilePrototype() {
		return missile;
	}

	/**
	 * Sets missile to use as prototype.
	 * 
	 * @param missile
	 *            new missile prototype.
	 */
	void setMissile(Missile missile) {
		this.missile = missile;
	}

	/**
	 * Returns damage that is dealt by this weapon.
	 * 
	 * @return weapon damage.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Returns speed of missile used by this weapon. This is the speed of
	 * missile prototype.
	 * 
	 * @return missile speed.
	 */
	public double getMissileSpeed() {
		return missile.getSpeed();
	}

	/**
	 * Sets missile speed of default missile.
	 * 
	 * @param missileSpeed
	 *            new missile speed.
	 */
	public void setMissileSpeed(double missileSpeed) {
		missile.setSpeed(missileSpeed);
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public long getCooldownTime() {
		return defaultCooldown;
	}

	@Override
	public void reduceCooldown(long value) {
		cooldown = Math.max(0, cooldown - value);
	}

	@Override
	public void resetCooldown() {
		cooldown = 0;
	}

	@Override
	public void use(ItemUseInformation info) {
		cooldown = defaultCooldown;
	}

	@Override
	public boolean hasCooldown() {
		if (cooldown > 0)
			return true;
		else
			return false;
	}

	/**
	 * Spawns a missile that is used by this weapon. Use this method for
	 * shooting. It creates a new missile that is identical to missile
	 * prototype.
	 * 
	 * @return duplicate of missile prototype.
	 * @throws NoAmmunitionException
	 */
	public Missile getAMissile() throws NoAmmunitionException {
		return missile.spawn();
	}

}
