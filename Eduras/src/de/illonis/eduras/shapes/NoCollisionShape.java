package de.illonis.eduras.shapes;

import java.util.LinkedList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

public class NoCollisionShape extends ObjectShape {

	@Override
	public Vector2D checkCollision(GameInformation game, GameObject thisObject,
			Vector2D target) {
		return target;
	}

	@Override
	public LinkedList<Vector2D> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.shapes.ObjectShape#getBorderLines()
	 */
	@Override
	public LinkedList<Line> getBorderLines() {
		return null;
	}

}
