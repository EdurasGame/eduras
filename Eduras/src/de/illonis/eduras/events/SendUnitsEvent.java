package de.illonis.eduras.events;

import java.util.LinkedList;

import de.illonis.eduras.math.Vector2D;

/**
 * Event that describes a unit sending command.
 * 
 * @author illonis
 * 
 */
public class SendUnitsEvent extends OwnerGameEvent {
	private final Vector2D target;
	private final LinkedList<Integer> units;

	/**
	 * @param playerId
	 *            the owner id of the sending player.
	 * @param target
	 *            the target location.
	 * @param units
	 *            one or multiple unit ids.
	 */
	public SendUnitsEvent(int playerId, Vector2D target, int... units) {
		super(GameEventNumber.SEND_UNITS, playerId);
		this.target = new Vector2D(target);
		this.units = new LinkedList<Integer>();
		for (int i : units) {
			this.units.add(i);
		}
	}

	/**
	 * @return the target location.
	 */
	public Vector2D getTarget() {
		return target;
	}

	/**
	 * @return the units commanded.
	 */
	public LinkedList<Integer> getUnits() {
		return units;
	}

}
