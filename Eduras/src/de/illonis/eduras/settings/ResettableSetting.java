package de.illonis.eduras.settings;

/**
 * A setting that has reset function.
 * 
 * @author illonis
 * 
 */
public abstract class ResettableSetting {

	/**
	 * Resets setting to default value.
	 */
	abstract void loadDefaults();
}
