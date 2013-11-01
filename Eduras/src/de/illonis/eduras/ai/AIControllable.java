package de.illonis.eduras.ai;

/**
 * Indicates that a unit is controllable by AI.
 * 
 * @author illonis
 * 
 */
public interface AIControllable {

	/**
	 * @return the AI for this unit.
	 */
	UnitAI getAI();

}
