package de.illonis.eduras;

import de.illonis.eduras.ObjectFactory.ObjectType;

public class FactoryException extends Exception {

	public FactoryException(ObjectType type) {
		super("Object of type " + type + " is not supported by objectfactory.");
	}
}
