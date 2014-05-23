package de.illonis.eduras.items.weapons;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;

/**
 * An {@link Item} that is {@link Usable}. This class implements the cooldown
 * handling and leaves the use method not implemented.
 * 
 * @author Florian 'Ren' Mai
 * 
 */
public abstract class UsableItem extends Item implements Usable {

	private final static Logger L = EduLog.getLoggerFor(UsableItem.class
			.getName());

	private long cooldown = 0;
	protected long defaultCooldown = 0;

	/**
	 * Create a new usable item
	 * 
	 * @param type
	 * @param timingSource
	 * @param gi
	 * @param id
	 */
	public UsableItem(ObjectType type, TimingSource timingSource,
			GameInformation gi, int id) {
		super(type, timingSource, gi, id);
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
	public boolean hasCooldown() {
		return (cooldown > 0);
	}
}
