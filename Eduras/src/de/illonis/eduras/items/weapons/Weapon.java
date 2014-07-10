package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.GeomUtil;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A weapon holds a missile prototype that is used for shooting. This is wrong!
 * 
 * @author illonis
 * 
 */
public abstract class Weapon extends UsableItem implements Lootable {

	protected enum AmmunitionLimit {
		INFINITE, CAPPED;
	}

	private AmmunitionLimit ammuType = AmmunitionLimit.INFINITE;
	private int currentAmmunition = 0;
	private int fillAmmunitionAmount = 0;
	private int maxAmmunition = -1;
	protected long respawnTime = S.Server.go_weapon_respawntime_default;
	private long respawnTimeRemaining = 0;

	/**
	 * Creates a new weapon being of the type given.
	 * 
	 * @param type
	 *            the objecttype of the weapon.
	 * @param gi
	 *            The gameinformation context
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id of the new weapon.
	 */
	public Weapon(ObjectType type, GameInformation gi,
			TimingSource timingSource, int id) {
		super(type, timingSource, gi, id);
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
	 * @return maximum ammunition.
	 */
	public int getMaxAmmunition() {
		return maxAmmunition;
	}

	/**
	 * @param currentAmmunition
	 *            the new amount.
	 */
	public void setCurrentAmmunition(int currentAmmunition) {
		this.currentAmmunition = currentAmmunition;
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
		if (!getGame().getGameSettings().getGameMode().doItemsRespawn()) {
			return false;
		}

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

		Vector2f target = info.getTarget();
		GameObject triggeringObject = info.getTriggeringObject();

		Vector2f center = new Vector2f(triggeringObject.getPositionVector());

		Vector2f speedVector = new Vector2f(target);
		speedVector.sub(center);

		Vector2f diag = new Vector2df(triggeringObject.getShape().getWidth(),
				triggeringObject.getShape().getHeight());
		Vector2f copy = speedVector.copy().normalise().scale(diag.length());
		center.add(copy);

		getGame().getEventTriggerer().createMissile(missileType, getOwner(),
				center, speedVector);
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
