package de.illonis.eduras.mapeditor;

/**
 * An entity that can be referenced by name.
 * 
 * @author illonis
 * 
 */
public interface Referencable {

	/**
	 * @return the reference name.
	 */
	String getRefName();

	/**
	 * Sets the reference name.
	 * 
	 * @param refName
	 *            the new name.
	 */
	void setRefName(String refName);
}
