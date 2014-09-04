package de.illonis.eduras;

import de.illonis.eduras.mapeditor.Referencable;

/**
 * An concrete entity that can be referenced by name.
 * 
 * @author illonis
 * 
 */
public abstract class ReferencedEntity implements Referencable {

	private String refName;

	@Override
	public final String getRefName() {
		return refName;
	}

	@Override
	public final void setRefName(String refName) {
		this.refName = refName;
	}
}
