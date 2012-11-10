/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape;

/**
 * A unit test class for shapes.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ShapeTests {

	@Test
	public void getAbsoluteVertices() {
		Player player = new Player(new GameInformation(), 0);
		player.setPosition(5, 5);

		ObjectShape playerShape = player.getShape();
		LinkedList<Vector2D> absoluteVertices = playerShape
				.getAbsoluteVertices(player);

		assertTrue(new Vector2D(5, 15).equals(absoluteVertices.get(0)));
		assertTrue(new Vector2D(15, -5).equals(absoluteVertices.get(1)));
		assertTrue(new Vector2D(-5, -5).equals(absoluteVertices.get(2)));

	}
}