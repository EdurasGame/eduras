package de.illonis.eduras.events;

/**
 * A event that indicates the death of a unit.
 * 
 * @author illonis
 * 
 */
public class DeathEvent extends GameEvent {
	int killerOwner;
	int killed;

	/**
	 * Creates a new death event.
	 * 
	 * @param killed
	 *            id of killed unit.
	 * @param killerOwner
	 *            owner of killing object.
	 */
	public DeathEvent(int killed, int killerOwner) {
		super(GameEventNumber.DEATH);
		this.killed = killed;
		this.killerOwner = killerOwner;

		putArgument(killed);
		putArgument(killerOwner);
	}

	/**
	 * Returns the owner id of the killing object.
	 * 
	 * @return owner id of killer.
	 * 
	 * @author illonis
	 */
	public int getKillerOwner() {
		return killerOwner;
	}

	/**
	 * Returns the object id of killed unit.
	 * 
	 * @return units object id.
	 * 
	 * @author illonis
	 */
	public int getKilled() {
		return killed;
	}

}