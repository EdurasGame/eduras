package de.illonis.eduras.exceptions;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class NoSpawnAvailableException extends Exception {

	public NoSpawnAvailableException(Vector2f point, Shape objectShape) {
		super("Could not spawn at " + point.toString() + " with shape "
				+ objectShape);
	}

	public NoSpawnAvailableException(Shape spawnArea, Shape objectShape) {
		super("Couldn't spawn in spawnarea " + spawnArea + " with shape "
				+ objectShape);
	}
}
