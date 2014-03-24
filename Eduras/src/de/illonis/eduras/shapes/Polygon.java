package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.PointNotOnCircleException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapes.data.ShapeParser;
import de.illonis.eduras.utils.Pair;

/**
 * 
 * Represents a Polygon.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
@Deprecated
public class Polygon extends ObjectShape {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	/**
	 * Determines how exactly collisions are calculated. The higher this value,
	 * the less will gameobjects of type triangle will be able to overlap.
	 */
	private static final int COLLISION_ACCURACY = 20;

	private Vector2df[] vertices;

	/**
	 * Creates a polygon with no vertices.
	 */
	public Polygon() {
		this(new Vector2df[0]);
	}

	/**
	 * Creates a new polygon with the given vertices.
	 * 
	 * @param vertices
	 */
	public Polygon(Vector2df[] vertices) {
		setVector2dfs(vertices);
	}

	/**
	 * Loads shapedata from file instead of setting it manually using the
	 * {@link #setVector2dfs(Vector2df[])} method.
	 * 
	 * @param shapeFileName
	 *            the name of the shapefile. Must be located in the
	 *            "shapes.data"-package.
	 * @throws FileCorruptException
	 *             if the shape file contains invalid data.
	 * @throws IOException
	 *             if there was an error reading the shape file.
	 */
	protected final void loadFromFile(String shapeFileName)
			throws FileCorruptException, IOException {
		Vector2df[] shapeVerts = ShapeParser.readShape(getClass().getResource(
				"data/" + shapeFileName));

		setVector2dfs(shapeVerts);
	}

	/**
	 * Set the array of vertices of this polygon to the given array and
	 * recalculates the boundingbox.
	 * 
	 * @param vertices
	 *            The array to set the vertices of this polygon to.
	 */
	public void setVector2dfs(Vector2df[] vertices) {
		this.vertices = vertices;
		calculateBoundingBox();
	}

	/**
	 * Returns the line segments which represents the borders of the shape. The
	 * borders are calculated in absolute values from the given object.
	 * 
	 * @param object
	 *            The object from which the absolute borderlines are calculated.
	 * @return The line segments representing the borders of the shape.
	 */
	public LinkedList<Line> getBorderLines(GameObject object) {
		return Geometry.getRelativeBorderLines(getAbsoluteVector2dfs(object)
				.toArray(new Vector2df[getAbsoluteVector2dfs(object).size()]));
	}

	/**
	 * Returns the vertices this shape is made of. Note that the relative values
	 * are returned!
	 * 
	 * @return the vertices.
	 * 
	 */
	public LinkedList<Vector2df> getVector2dfs() {
		LinkedList<Vector2df> result = new LinkedList<Vector2df>();
		for (int i = 0; i < vertices.length; i++) {
			result.add(vertices[i]);
		}
		return result;
	}

	/**
	 * Returns the vertices as an array of vectors.
	 * 
	 * @return The vertices as an array.
	 */
	public Vector2df[] getVector2dfsAsArray() {
		return vertices;
	}

	/**
	 * Returns the absolute vertices of the shape assuming the shape belongs to
	 * the given object.
	 * 
	 * @param object
	 *            The object to which the shape belongs.
	 * @return Returns a LinkedList of absolute vertices.
	 */
	public LinkedList<Vector2df> getAbsoluteVector2dfs(GameObject object) {
		double objectXPos = object.getXPosition();
		double objectYPos = object.getYPosition();

		LinkedList<Vector2df> relativeVector2dfs = getVector2dfs();
		LinkedList<Vector2df> absoluteVector2dfs = new LinkedList<Vector2df>();

		for (Vector2df singleRelativeVertex : relativeVector2dfs) {

			Vector2df singleRelativeVertexCopy = new Vector2df(
					singleRelativeVertex);
			singleRelativeVertexCopy.rotate(object.getRotation());

			double absXPos = singleRelativeVertexCopy.getX() + objectXPos;
			double absYPos = singleRelativeVertexCopy.getY() + objectYPos;

			Vector2df singleAbsoluteVector2df = new Vector2df(absXPos, absYPos);

			absoluteVector2dfs.add(singleAbsoluteVector2df);
		}

		return absoluteVector2dfs;
	}

	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {
		LinkedList<CollisionPoint> interceptPoints = new LinkedList<CollisionPoint>();

		for (Line line : lines) {
			for (Line borderLine : getBorderLines(thisObject)) {
				Vector2df interceptPoint = Geometry
						.getSegmentLinesInterceptPoint(borderLine, line);

				if (interceptPoint == null) {
					continue;
				} else {

					CollisionPoint interception = CollisionPoint
							.createCollisionPointByInterceptPoint(
									interceptPoint, line);
					interceptPoints.add(interception);
				}

			}
		}
		return interceptPoints;
	}

	private void calculateBoundingBox() {
		if (vertices.length == 0) {
			boundingBox = new Rectangle2D.Double();
			return;
		}
		ArrayList<Double> xValues = new ArrayList<Double>();
		ArrayList<Double> yValues = new ArrayList<Double>();
		for (Vector2df vertex : vertices) {
			xValues.add(vertex.getX());
			yValues.add(vertex.getY());
		}

		double maxX = BasicMath.max(xValues.toArray(new Double[1]));
		double maxY = BasicMath.max(yValues.toArray(new Double[1]));
		double minX = BasicMath.min(xValues.toArray(new Double[1]));
		double minY = BasicMath.min(yValues.toArray(new Double[1]));
		boundingBox = new Rectangle2D.Double(minX, minY, maxX - minX, maxY
				- minY);
	}

	@Override
	public Vector2df[] getBorderPoints() {
		LinkedList<Line> borderLines = Geometry
				.getRelativeBorderLines(vertices);

		Vector2df[] movementPoints = new Vector2df[COLLISION_ACCURACY
				* vertices.length];

		int j = 0;
		for (Line singleBorderLine : borderLines) {
			for (int i = 0; i < COLLISION_ACCURACY; i++) {
				movementPoints[j] = singleBorderLine
						.getPointAt((1. / COLLISION_ACCURACY) * i);
				j++;
			}
		}

		return movementPoints;
	}

	@Override
	public ObjectShape getScaled(double scale) {
		// TODO: implement
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Polygon) {
			Polygon p = (Polygon) obj;
			if (getVector2dfs().isEmpty())
				return p.getVector2dfs().isEmpty();
			return p.getVector2dfs().equals(getVector2dfs());
		} else
			return super.equals(obj);
	}

	@Override
	public double checkCollisionOnRotation(GameInformation gameInfo,
			GameObject thisObject, double targetRotationAngle) {

		if (!thisObject.isCollidable()) {
			return targetRotationAngle;
		}

		double rotationDiff = targetRotationAngle - thisObject.getRotation();

		Vector2df posVector = thisObject.getPositionVector();

		double distance = BasicMath.findShortestDistanceModulo(
				targetRotationAngle, thisObject.getRotation(), 360);
		boolean turnLeft = doesTurnLeft(distance);

		LinkedList<Vector2df> interceptPoints = new LinkedList<Vector2df>();
		for (GameObject anotherGameObject : gameInfo.getObjects().values()) {
			interceptPoints.addAll(calculateInterceptPointsWithObject(
					anotherGameObject, thisObject, turnLeft, rotationDiff));
		}

		if (interceptPoints.isEmpty()) {
			return targetRotationAngle;
		} else {

			// return thisObject.getRotation();
			// Find the point that comes first when rotating (where the angle
			// interceptpointAngle - currAngle is smallest)
			// LinkedList<Double> angles = new LinkedList<Double>();
			// for (int i = 0; i < interceptPoints.size(); i++) {
			// angles.add(posVector
			// .getDistanceVectorTo(interceptPoints.get(i))
			// .getAngleToXAxis());
			// }
			double[] angles = new double[interceptPoints.size()];
			for (int i = 0; i < interceptPoints.size(); i++) {
				angles[i] = posVector.getDistanceVectorTo(
						interceptPoints.get(i)).getAngleToXAxis();
			}

			double closestCollisionAngle = angles[BasicMath
					.findClosestNumberModuloArray(targetRotationAngle, angles,
							360)];

			// boolean closestAngleFound = false;
			// while (!closestAngleFound) {
			// double[] anglesCopy = new double[angles.size()];
			// for (int i = 0; i < angles.size(); i++) {
			// anglesCopy[i] = angles.get(i);
			// }
			//
			// int indexOfCloesest;
			// try {
			// indexOfCloesest = BasicMath.findClosestNumberModuloArray(
			// thisObject.getRotation(), anglesCopy, 360);
			// } catch (IllegalArgumentException e) {
			// // here, non of the given points is a valid point, so return
			// return thisObject.getRotation();
			// }
			// closestCollisionAngle = anglesCopy[BasicMath
			// .findClosestNumberModuloArray(thisObject.getRotation(),
			// anglesCopy, 360)];
			//
			// if (turnLeft
			// && !BasicMath
			// .isInBetweenModulo(thisObject.getRotation(),
			// closestCollisionAngle,
			// targetRotationAngle, 360)) {
			// angles.remove(indexOfCloesest);
			// continue;
			// } else {
			// if (!turnLeft
			// && !BasicMath.isInBetweenModulo(
			// targetRotationAngle, closestCollisionAngle,
			// thisObject.getRotation(), 360)) {
			// angles.remove(indexOfCloesest);
			// continue;
			// }
			// }
			//
			// closestAngleFound = true;
			// }

			// stop a bit before the first collision
			if (turnLeft) {
				return BasicMath.calcModulo(closestCollisionAngle - 5, 360);
			} else {
				return BasicMath.calcModulo(closestCollisionAngle + 5, 360);
			}

			// return BasicMath.findMiddleOfPointsModulo(closCollisionAngle,
			// thisObject.getRotation(), 360);
		}
	}

	private Collection<? extends Vector2df> calculateInterceptPointsWithObject(
			GameObject anotherGameObject, GameObject thisObject,
			boolean turnLeft, double rotationDiff) {
		Vector2df posVector = thisObject.getPositionVector();
		LinkedList<Vector2df> thisObjectAbsoluteVector2dfs = getAbsoluteVector2dfs(thisObject);

		LinkedList<Vector2df> interceptPointsWithGameObject = new LinkedList<Vector2df>();

		if (anotherGameObject.equals(thisObject)
				|| !anotherGameObject.isCollidable()) {
			return interceptPointsWithGameObject;
		}

		for (Vector2df anAbsoluteVertex : thisObjectAbsoluteVector2dfs) {
			Circle aRotationCircle = Geometry
					.getCircleByCenterAndPointOnCircle(posVector,
							anAbsoluteVertex);

			// circle, forget about this at the moment, as only polygon is
			// implemented so far
			if (anotherGameObject.getShape() instanceof Circle) {
				Pair<Vector2df, Vector2df> circleIntercepts = Geometry
						.getInterceptPointsOfCircles(
								(Circle) anotherGameObject.getShape(),
								anotherGameObject.getPositionVector(),
								aRotationCircle, thisObject.getPositionVector());
				if (circleIntercepts != null) {
					interceptPointsWithGameObject.add(circleIntercepts
							.getFirst());
					interceptPointsWithGameObject.add(circleIntercepts
							.getSecond());
				}
			}
			// polygon
			else {
//				if (anotherGameObject.getShape() instanceof Polygon) {
//					Polygon othersShape = (Polygon) anotherGameObject
//							.getShape();
//					for (Line anotherObjectsBorderLine : othersShape
//							.getBorderLines(anotherGameObject)) {
//						Vector2df[] polygonInterceptPoints = Geometry
//								.getCircleLineSegmentInterceptPoints(
//										aRotationCircle, posVector,
//										anotherObjectsBorderLine);
//						for (int i = 0; i < polygonInterceptPoints.length; i++) {
//							if (polygonInterceptPoints[i] != null) {
//								interceptPointsWithGameObject
//										.add(polygonInterceptPoints[i]);
//							}
//						}
//					}
//				} else {
//					L.severe("Can't calculate collision with objectshape "
//							+ anotherGameObject.getShape().getClass()
//									.getSimpleName());
//					break;
//				}

			}

			// Remove all intercept points, that are not on the circle
			// between
			// target angle and current angle
			double currentAngleOfVertex;
			try {
				currentAngleOfVertex = Geometry.getAngleForPointOnCircle(
						aRotationCircle, posVector, anAbsoluteVertex);
			} catch (PointNotOnCircleException e1) {
				L.severe("Point we used to create the circle is not on circle!");
				continue;
			}

			// make copy to avoid concurrentmodificationexception
			LinkedList<Vector2df> copyOfInterceptPoints = new LinkedList<Vector2df>(
					interceptPointsWithGameObject);
			for (Vector2df anInterceptPoint : copyOfInterceptPoints) {

				try {
					double angle = Geometry.getAngleForPointOnCircle(
							aRotationCircle, posVector, anInterceptPoint);
					// if we turn left, the angle 'increases' modulo 360. so we
					// need to check if it's between current- and target angle
					// if we turn right, the angle 'decreases modulo 360'. so we
					// have to check if it's between target- and current angle
					if (!((turnLeft && BasicMath.isInBetweenModulo(
							currentAngleOfVertex,
							angle,
							BasicMath.calcModulo(currentAngleOfVertex
									+ rotationDiff, 360), 360)) || !turnLeft
							&& BasicMath.isInBetweenModulo(
									BasicMath.calcModulo(currentAngleOfVertex
											+ rotationDiff, 360), angle,
									currentAngleOfVertex, 360))) {
						interceptPointsWithGameObject.remove(anInterceptPoint);
					}
				} catch (PointNotOnCircleException e) {
					L.warning("There is a point that is not on the circle, which shouldn't appear from previous calculation.");
					interceptPointsWithGameObject.remove(anInterceptPoint);
				}
			}
		}

		return interceptPointsWithGameObject;
	}

	private boolean doesTurnLeft(double distance) {
		// for the following calculation imagine a circle. In order to get to
		// the target angle, you have to rotate at most by 180 degrees, either
		// left or right. If the shortest rotation passes the point where the
		// angle is 0, abs(targetAngle - currAngle) must be > 180. Then we can
		// decide by the distance's sign whether we have to turn right or left.
		if (Math.abs(distance) > 180) {
			if (distance > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			if (distance > 0) {
				return true;
			} else {
				return false;
			}
		}
	}
}
