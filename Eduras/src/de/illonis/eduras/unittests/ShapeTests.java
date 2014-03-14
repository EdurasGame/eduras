/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Polygon;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A unit test class for shapes.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ShapeTests {

	/**
	 * Tests the
	 * {@link de.illonis.eduras.shapes.Polygon#getAbsoluteVertices(de.illonis.eduras.gameobjects.GameObject)
	 * getAbsoluteVertices()} method.
	 */
	@Test
	public void getAbsoluteVertices() {
		PlayerMainFigure player = new PlayerMainFigure(new GameInformation(),
				null, 0, 0);
		player.setPosition(5, 5);

		if (player.getShape() instanceof Polygon) {
			Polygon playerShape = (Polygon) player.getShape();
			LinkedList<Vector2D> absoluteVertices = playerShape
					.getAbsoluteVertices(player);

			assertTrue(new Vector2D(30, 5).equals(absoluteVertices.get(0)));
			assertTrue(new Vector2D(-5, 15).equals(absoluteVertices.get(1)));
			assertTrue(new Vector2D(-5, -5).equals(absoluteVertices.get(2)));
		}

	}

	/**
	 * Tests the
	 * {@link de.illonis.eduras.shapes.Polygon#checkCollisionOnRotation(GameInformation, de.illonis.eduras.gameobjects.GameObject, double)}
	 * method.
	 */
	@Test
	public void getCollisionOnRotation() {

		// //////////// PREPERATION
		GameInformation gameInfo = new GameInformation();
		BigBlock otherObject = null;
		try {
			otherObject = new BigBlock(gameInfo, null, 1);
		} catch (ShapeVerticesNotApplicableException e) {
			assertTrue(false);
		}
		otherObject.setPosition(50, 50);

		PlayerMainFigure thisObject = new PlayerMainFigure(gameInfo, null, 3,
				"Testobject", 2);

		thisObject.setPosition(50, 29);

		gameInfo.addObject(thisObject);
		gameInfo.addObject(otherObject);

		// ////////// PREPERATION END

		thisObject.setRotation(90.);

		System.out.println("Rotation at: " + thisObject.getRotation());
		assertTrue(thisObject.getRotation() != 90.);

	}
}
