package de.illonis.eduras.events;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.actions.ScoutSpellAction;

/**
 * Respective event for {@link ScoutSpellAction}.
 * 
 * @author illonis
 * 
 */
public class ScoutSpellEvent extends OwnerGameEvent {

	private Vector2f target;

	/**
	 * Creates the event.
	 * 
	 * @param executingPlayer
	 *            the player to execute the spell
	 * @param target
	 *            the location to give vision at
	 */
	public ScoutSpellEvent(int executingPlayer, Vector2f target) {
		super(GameEventNumber.SPELL_SCOUT, executingPlayer);
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
