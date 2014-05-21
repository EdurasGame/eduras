//package de.illonis.eduras.unittests;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.newdawn.slick.geom.Vector2f;
//
//import de.illonis.eduras.math.Line;
//import de.illonis.eduras.math.Vector2df;
//
///**
// * Unittest class for Line.
// * 
// * @author Florian Mai <florian.ren.mai@googlemail.com>
// * 
// */
//public class LineUnitTest {
//
//	Line horizontallineThroughOrigin;
//	Line fourtyFiveDegreesLineThroughOrigin;
//	Line randomLine;
//
//	/**
//	 * Creates some lines needed before each test method is executed.
//	 */
//	@Before
//	public void init() {
//		horizontallineThroughOrigin = new Line(new Vector2df(1, 0),
//				new Vector2df(-1, 0));
//		fourtyFiveDegreesLineThroughOrigin = new Line(new Vector2df(-1, -1),
//				new Vector2df(1, 1));
//		randomLine = new Line(new Vector2df(-10.505f, 2), new Vector2df(0, 23));
//	}
//
//	/**
//	 * Tests the {@link de.illonis.eduras.math.Line#getSupportVector()
//	 * getSupportVector()} and
//	 * {@link de.illonis.eduras.math.Line#getDirectionalVector()
//	 * getDirectionVector()} methods. getSegmentLineInterceptPoint} method.
//	 */
//	@Test
//	public void supportAndDirectionVectorCreation() {
//
//		Vector2f directionalVec = horizontallineThroughOrigin
//				.getDirectionalVector();
//		Vector2f suppVec = horizontallineThroughOrigin.getSupportVector();
//		assertTrue(directionalVec.equals(new Vector2df(-2, 0)));
//		assertTrue(suppVec.equals(new Vector2df(1, 0)));
//
//		directionalVec = fourtyFiveDegreesLineThroughOrigin
//				.getDirectionalVector();
//		suppVec = fourtyFiveDegreesLineThroughOrigin.getSupportVector();
//		assertTrue(directionalVec.equals(new Vector2df(2, 2)));
//		assertTrue(suppVec.equals(new Vector2df(-1, -1)));
//
//		directionalVec = randomLine.getDirectionalVector();
//		suppVec = randomLine.getSupportVector();
//		assertTrue("The vector (" + directionalVec.getX() + ","
//				+ directionalVec.getY()
//				+ ") is not the same as the vector (10.505,21)",
//				directionalVec.equals(new Vector2df(10.505f, 21)));
//		assertTrue(suppVec.equals(new Vector2df(-10.505f, 2)));
//
//	}
//
//	/**
//	 * Tests the {@link de.illonis.eduras.math.Line#containsPoint(Vector2df)
//	 * containsPoint()} method.
//	 */
//	@Test
//	public void containsPointTest() {
//		assertTrue(horizontallineThroughOrigin
//				.containsPoint(new Vector2df(0, 0)));
//		assertTrue(horizontallineThroughOrigin.containsPoint(new Vector2df(30,
//				0)));
//		assertTrue(horizontallineThroughOrigin
//				.containsPoint(new Vector2df(1, 0)));
//
//		assertTrue(fourtyFiveDegreesLineThroughOrigin
//				.containsPoint(new Vector2df(1, 1)));
//		assertTrue(fourtyFiveDegreesLineThroughOrigin
//				.containsPoint(new Vector2df(0, 0)));
//		assertTrue(fourtyFiveDegreesLineThroughOrigin
//				.containsPoint(new Vector2df(10001.1001f, 10001.1001f)));
//
//		assertTrue(randomLine.containsPoint(new Vector2df(10.505f, 44)));
//		assertTrue(randomLine.containsPoint(new Vector2df(36.7675f, 96.5f)));
//		assertFalse(randomLine.containsPoint(new Vector2df(0, 0)));
//	}
//
// }
