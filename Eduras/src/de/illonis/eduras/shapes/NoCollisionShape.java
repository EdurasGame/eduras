package de.illonis.eduras.shapes;

import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

public class NoCollisionShape extends ObjectShape {

	@Override
	public Vector2D checkCollision(GameInformation game, GameObject thisObject, Vector2D target) {
		return target;
	}

	@Override
	public Vector2D isIntersected(LinkedList<Line> lines, GameObject thisObject) {
		return null;
	}

}
