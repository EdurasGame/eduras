package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A weapon holds a missile prototype that is used for shooting. This is wrong!
 * 
 * @author illonis
 * 
 */
public abstract class Weapon extends Item implements Lootable, Usable {

	protected enum AmmunitionLimit {
		INFINITE, CAPPED;
	}

	private AmmunitionLimit ammuType = AmmunitionLimit.INFINITE;
	private int currentAmmunition = 0;
	private int fillAmmunitionAmount = 0;
	private int maxAmmunition = -1;
	private long cooldown = 0;
	protected long defaultCooldown = 0;
	protected long respawnTime = S.go_weapon_respawntime_default;
	private long respawnTimeRemaining = 0;

	/**
	 * Creates a new weapon being of the type given.
	 * 
	 * @param type
	 *            the objecttype of the weapon.
	 * @param gi
	 *            The gameinformation context
	 * @param id
	 *            The id of the new weapon.
	 */
	public Weapon(ObjectType type, GameInformation gi, int id) {
		super(type, gi, id);
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public final long getCooldownTime() {
		return defaultCooldown;
	}

	@Override
	public final void reduceCooldown(long value) {
		cooldown = Math.max(0, cooldown - value);
	}

	@Override
	public final void resetCooldown() {
		cooldown = 0;
	}

	@Override
	public final void startCooldown() {
		cooldown = defaultCooldown;
	}

	@Override
	public final boolean use(ItemUseInformation info) {
		if (!hasCooldown()) {
			if (hasAmmo()) {
				startCooldown();
				currentAmmunition--;
				doIfReady(info);
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the ammunition-type of this weapon to infinite. This means this
	 * weapon's magazine never gets empty.
	 */
	protected final void setAmmunitionInfinite() {
		ammuType = AmmunitionLimit.INFINITE;
	}

	/**
	 * @return true if remaining ammunition is >0 or weapon has infinite
	 *         ammunition.
	 */
	public boolean hasAmmo() {
		return ammuType == AmmunitionLimit.INFINITE || currentAmmunition > 0;
	}

	/**
	 * Indicates that this weapon has limited ammunition.
	 * 
	 * @param fillAmount
	 *            the amount to fill up on every fill (and start value).
	 * @param maxAmount
	 *            the maximum of ammunition this weapon can have.
	 */
	protected final void setAmmunitionLimited(int fillAmount, int maxAmount) {
		ammuType = AmmunitionLimit.CAPPED;
		fillAmmunitionAmount = fillAmount;
		maxAmmunition = maxAmount;
		currentAmmunition = fillAmmunitionAmount;
	}

	/**
	 * Refills the ammunition of this weapon.<br>
	 * On each call, the ammunition is increased by {@link #currentAmmunition}
	 * until the maximum {@link #maxAmmunition} is reached.<br>
	 * <i>Has no effect on weapons with infinite ammunition.</i>
	 */
	public synchronized void refill() {
		if (ammuType == AmmunitionLimit.INFINITE)
			return;
		currentAmmunition += fillAmmunitionAmount;
		if (currentAmmunition > maxAmmunition)
			currentAmmunition = maxAmmunition;
	}

	/**
	 * Performs given action if weapon is ready. If weapon is not ready, no
	 * action will performed.<br>
	 * <i>Note:</i> The ammunition amount has already been decreased before this
	 * method is called.
	 * 
	 * @param info
	 *            item use information.
	 */
	protected abstract void doIfReady(ItemUseInformation info);

	@Override
	public boolean hasCooldown() {
		return (cooldown > 0);
	}

	@Override
	public long getRespawnTime() {
		return respawnTime;
	}

	@Override
	public long getRespawnTimeRemaining() {
		return respawnTimeRemaining;
	}

	/**
	 * @return the current amount of ammunition or -1 if weapon has infinite
	 *         ammunition.
	 */
	public int getCurrentAmmunition() {
		if (ammuType == AmmunitionLimit.INFINITE)
			return -1;
		return currentAmmunition;
	}

	@Override
	public boolean reduceRespawnRemaining(long value) {
		if (respawnTimeRemaining == 0)
			return false;
		respawnTimeRemaining = Math.max(0, respawnTimeRemaining - value);
		if (respawnTimeRemaining == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void loot() {
		respawnTimeRemaining = respawnTime;
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		if (collidingObject.getType() != ObjectType.PLAYER) {
			return;
		}

		PlayerMainFigure player = (PlayerMainFigure) collidingObject;
		getGame().getEventTriggerer().lootItem(getId(), player.getId());
	}

	/**
	 * Spawns ("shoots") a missile of given objecttype.
	 * 
	 * @param missileType
	 *            the type of missile.
	 * @param info
	 *            the use-information provided by
	 *            {@link #doIfReady(ItemUseInformation)} method.
	 */
	protected final void shootMissile(ObjectType missileType,
			ItemUseInformation info) {
		// (jme) Spawn position will be calculated in a simplified way. We use
		// diagonal's length of shooting player to move missile away from him.

		Vector2D target = info.getTarget();
		GameObject triggeringObject = info.getTriggeringObject();

		Vector2D position = triggeringObject.getPositionVector();

		Vector2D speedVector = new Vector2D(target);
		speedVector.subtract(position);

		Vector2D diag = new Vector2D(triggeringObject.getBoundingBox()
				.getWidth(), triggeringObject.getBoundingBox().getHeight());
		Vector2D copy = speedVector.copy();
		copy.setLength(diag.getLength());
		position.add(copy);

		getGame().getEventTriggerer().createMissile(missileType, getOwner(),
				position, speedVector);
	}

	/**
	 * Reduces ammunitiion by one if not infinite.
	 */
	public void reduceAmmo() {
		if (ammuType == AmmunitionLimit.INFINITE)
			return;
		currentAmmunition--;
	}
}
