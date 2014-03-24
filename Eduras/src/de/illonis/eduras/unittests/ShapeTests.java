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
import de.illonis.eduras.math.Vector2df;
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
	 * {@link de.illonis.eduras.shapes.Polygon#getAbsoluteVector2dfs(de.illonis.eduras.gameobjects.GameObject)
	 * getAbsoluteVector2dfs()} method.
	 */
	@Test
	public void getAbsoluteVector2dfs() {
		PlayerMainFigure player = new PlayerMainFigure(new GameInformation(),
				null, 0, 0);
		player.setPosition(5, 5);

		if (player.getShape() instanceof Polygon) {
			Polygon playerShape = (Polygon) player.getShape();
			LinkedList<Vector2df> absoluteVector2dfs = playerShape
					.getAbsoluteVector2dfs(player);

			assertTrue(new Vector2df(30, 5).equals(absoluteVector2dfs.get(0)));
			assertTrue(new Vector2df(-5, 15).equals(absoluteVector2dfs.get(1)));
			assertTrue(new Vector2df(-5, -5).equals(absoluteVector2dfs.get(2)));
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
