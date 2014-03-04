package de.illonis.eduras.interfaces;

/**
 * Indicates that a setting is resettable and provides a reset method that sets
 * default values.
 * 
 * @author illonis
 * 
 */
public interface ResettableSetting {

	/**
	 * Resets setting to default value.
	 */
	public void loadDefaults();
}
