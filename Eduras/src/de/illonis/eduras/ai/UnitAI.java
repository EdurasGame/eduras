package de.illonis.eduras.ai;

/**
 * A basic Unit Artificial Intelligence.
 * 
 * @author illonis
 * 
 */
public interface UnitAI {

	/**
	 * @return the unit this AI controls.
	 */
	public AIControllable getUnit();

	/**
	 * Discards this AI and indicates it is no longer used.<br>
	 * This method terminates all running threads invoked by this AI.
	 */
	public void discard();
}
