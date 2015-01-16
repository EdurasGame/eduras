package de.illonis.eduras.gameclient;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ActionFailedException;

public class CantSpawnHereException extends ActionFailedException {

	private static final long serialVersionUID = 1L;

	public CantSpawnHereException(ObjectType objectType) {
		super("Can't spawn an object of type " + objectType + " here.");
	}

}
