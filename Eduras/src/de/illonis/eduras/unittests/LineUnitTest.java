/**
 * 
 */
package de.illonis.eduras.unittests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;

/**
 * Unittest class for Line.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class LineUnitTest {

	Line horizontallineThroughOrigin;
	Line fourtyFiveDegreesLineThroughOrigin;

	@Before
	public void init() {
		horizontallineThroughOrigin = new Line(new Vector2D(1, 0),
				new Vector2D(-1, 0));
		fourtyFiveDegreesLineThroughOrigin = new Line(new Vector2D(-1, -1),
				new Vector2D(1, 1));
	}

	@Test
	public void supportAndDirectionVectorCreation() {

		Vector2D directionalVec = horizontallineThroughOrigin
				.getDirectionalVector();
		Vector2D suppVec = horizontallineThroughOrigin.getSupportVector();
		assertTrue(directionalVec.equals(new Vector2D(-2, 0)));
		assertTrue(suppVec.equals(new Vector2D(1, 0)));

		directionalVec = fourtyFiveDegreesLineThroughOrigin
				.getDirectionalVector();
		suppVec = fourtyFiveDegreesLineThroughOrigin.getSupportVector();
		assertTrue(directionalVec.equals(new Vector2D(2, 2)));
		assertTrue(suppVec.equals(new Vector2D(-1, -1)));

	}

	@Test
	public void containsPointTest() {
		assertTrue(horizontallineThroughOrigin
				.containsPoint(new Vector2D(0, 0)));
		assertTrue(horizontallineThroughOrigin
				.containsPoint(new Vector2D(30, 0)));
		assertTrue(horizontallineThroughOrigin
				.containsPoint(new Vector2D(1, 0)));

		assertTrue(fourtyFiveDegreesLineThroughOrigin
				.containsPoint(new Vector2D(1, 1)));
		assertTrue(fourtyFiveDegreesLineThroughOrigin
				.containsPoint(new Vector2D(0, 0)));
		assertTrue(fourtyFiveDegreesLineThroughOrigin
				.containsPoint(new Vector2D(10001.1001, 10001.1001)));
	}
}
