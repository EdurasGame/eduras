package de.illonis.eduras.ai;

/**
 * Indicates that a unit is controllable by AI.<br>
 * This is a very general interface and meant to be extended by other
 * interfaces.
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
