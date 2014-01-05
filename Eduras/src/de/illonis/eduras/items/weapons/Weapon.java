package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A weapon holds a missile prototype that is used for shooting. This is wrong!
 * 
 * @author illonis
 * 
 */
public abstract class Weapon extends Item implements Lootable, Usable {
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
	public final void use(ItemUseInformation info) {
		if (!hasCooldown()) {
			startCooldown();
			doIfReady(info);
		}
	}

	/**
	 * Performs given action if weapon is ready. If weapon is not ready, no
	 * action will performed.
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
}
