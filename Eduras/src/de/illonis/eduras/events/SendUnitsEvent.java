package de.illonis.eduras.events;

import java.util.LinkedList;

import org.newdawn.slick.geom.Vector2f;

import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;

/**
 * Event that describes a unit sending command.
 * 
 * @author illonis
 * 
 */
public class SendUnitsEvent extends OwnerGameEvent {
	private final Vector2f target;
	private final LinkedList<Integer> units;

	/**
	 * @param playerId
	 *            the owner id of the sending player.
	 * @param target
	 *            the target location.
	 * @param units
	 *            one or multiple unit ids.
	 */
	public SendUnitsEvent(int playerId, Vector2f target,
			LinkedList<Integer> units) {
		super(GameEventNumber.SEND_UNITS, playerId);
		this.target = new Vector2f(target);
		this.units = new LinkedList<Integer>(units);
	}

	/**
	 * @return the target location.
	 */
	public Vector2f getTarget() {
		return target;
	}

	/**
	 * @return the units commanded.
	 */
	public LinkedList<Integer> getUnits() {
		return units;
	}

	@Override
	public Object getArgument(int i) throws TooFewArgumentsExceptions {
		if (i == 0)
			return super.getArgument(i);
		if (i == 1)
			return target.getX();
		if (i == 2)
			return target.getY();
		if (i >= getNumberOfArguments()) {
			throw new TooFewArgumentsExceptions(i, getNumberOfArguments());
		}
		return units.get(i - 3);
	}

	@Override
	public int getNumberOfArguments() {
		return units.size() + 2;
	}

}
