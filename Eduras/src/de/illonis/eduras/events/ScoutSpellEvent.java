package de.illonis.eduras.events;

import org.newdawn.slick.geom.Vector2f;

/**
 * Spawns a scout.
 * 
 * @author illonis
 * 
 */
public class ScoutSpellEvent extends OwnerGameEvent {

	private Vector2f target;

	public ScoutSpellEvent(int owner, Vector2f target) {
		super(GameEventNumber.SPELL_SCOUT, owner);
		this.target = target;
		putArgument(target.x);
		putArgument(target.y);
	}

	/**
	 * @return the target location.
	 */
	public Vector2f getTarget() {
		return target;
	}
}
