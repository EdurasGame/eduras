package de.illonis.eduras.units;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.TimingSource;

/**
 * A unit is a moveable object that has health. A unit can never have negative
 * health or more health than {@link #maxHealth}.
 * 
 * @author illonis
 * 
 */
public abstract class Unit extends MoveableGameObject {

	private int health, maxHealth;
	private Team team;

	/**
	 * Creates a new unit with given maximum health. It's health is equal to
	 * maximum health at beginning.
	 * 
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param maxHealth
	 *            maximum health. Must be >0. Will be set to 1 otherwise.
	 * @param id
	 *            id of unit.
	 */
	public Unit(GameInformation game, TimingSource timingSource, int maxHealth,
			int id) {
		super(game, timingSource, id);
		if (maxHealth <= 0)
			maxHealth = 1;
		this.health = this.maxHealth = maxHealth;
	}

	/**
	 * @return unit's team.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Assigns unit to given team.
	 * 
	 * @param team
	 *            new team.
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Returns unit's maximum health.
	 * 
	 * @return maximum health.
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Fills health up to maximum health.
	 * 
	 * @author illonis
	 */
	public void resetHealth() {
		health = maxHealth;
	}

	/**
	 * Returns unit's current health.
	 * 
	 * @return current health.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets current health of this unit.
	 * 
	 * @param health
	 *            new current health. Must be <code>0<=health<=maxHealth</code>,
	 *            otherwise it will be adjusted to fit.
	 */
	public void setHealth(int health) {
		this.health = Math.min(Math.max(health, 0), maxHealth);
	}

	/**
	 * Sets maximum health of unit. Current health will scale percental.
	 * 
	 * @param maxHealth
	 *            maximum health. Must be greater than 0.
	 */
	public void setMaxHealth(int maxHealth) {
		if (maxHealth <= 0)
			return;
		float percent = maxHealth / (float) this.maxHealth;
		this.maxHealth = maxHealth;
		this.health *= percent;
	}

	/**
	 * Checks whether this unit is dead.
	 * 
	 * @return true if unit is dead.
	 */
	public boolean isDead() {
		return (health == 0);
	}

	/**
	 * Damages this unit with given damage. A unit's health cannot be less than
	 * 0.
	 * 
	 * @param damage
	 *            damage (must be >0).
	 * @param damager
	 *            the owner id of the object which caused the damage.
	 */
	public void damagedBy(int damage, int damager) {
		if (damage <= 0)
			return;
		int newHealth = Math.max(getHealth() - damage, 0);
		getGame().getEventTriggerer().setHealth(this.getId(), newHealth);
		if (isDead()) {
			getGame().getEventTriggerer().notifyDeath(this, damager);
			getGame().getGameSettings().getGameMode().onDeath(this, damager);
			onDead(damager);
		}
	}

	/**
	 * Called when unit dies.
	 * 
	 * @param killer
	 *            Specifies who killed this object. -1 if there is no killer.
	 */
	protected abstract void onDead(int killer);
}
