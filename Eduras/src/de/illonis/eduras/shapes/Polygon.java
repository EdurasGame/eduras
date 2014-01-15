package de.illonis.eduras.shapes;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.exceptions.PointNotOnCircleException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.math.CollisionPoint;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;
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
public class Polygon extends ObjectShape {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	/**
	 * Determines how exactly collisions are calculated. The higher this value,
	 * the less will gameobjects of type triangle will be able to overlap.
	 */
	private static final int COLLISION_ACCURACY = 20;

	private Vector2D[] vertices;

	/**
	 * Creates a polygon with no vertices.
	 */
	public Polygon() {
		this.vertices = new Vector2D[0];
	}

	/**
	 * Creates a new polygon with the given vertices.
	 * 
	 * @param vertices
	 */
	public Polygon(Vector2D[] vertices) {

		this.vertices = vertices;

	}

	/**
	 * Loads shapedata from file instead of setting it manually using the
	 * {@link #setVertices(Vector2D[])} method.
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
		Vector2D[] shapeVerts = ShapeParser.readShape(getClass().getResource(
				"data/" + shapeFileName));

		setVertices(shapeVerts);
	}

	/**
	 * Set the array of vertices of this polygon to the given array.
	 * 
	 * @param vertices
	 *            The array to set the vertices of this polygon to.
	 */
	public void setVertices(Vector2D[] vertices) {
		this.vertices = vertices;
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
		return Geometry.getRelativeBorderLines(getAbsoluteVertices(object)
				.toArray(new Vector2D[getAbsoluteVertices(object).size()]));
	}

	/**
	 * Returns the vertices this shape is made of. Note that the relative values
	 * are returned!
	 * 
	 * @return the vertices.
	 * 
	 */
	public LinkedList<Vector2D> getVertices() {
		LinkedList<Vector2D> result = new LinkedList<Vector2D>();
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
	public Vector2D[] getVerticesAsArray() {
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
	public LinkedList<Vector2D> getAbsoluteVertices(GameObject object) {
		double objectXPos = object.getXPosition();
		double objectYPos = object.getYPosition();

		LinkedList<Vector2D> relativeVertices = getVertices();
		LinkedList<Vector2D> absoluteVertices = new LinkedList<Vector2D>();

		for (Vector2D singleRelativeVertex : relativeVertices) {

			Vector2D singleRelativeVertexCopy = new Vector2D(
					singleRelativeVertex);
			singleRelativeVertexCopy.rotate(object.getRotation());

			double absXPos = singleRelativeVertexCopy.getX() + objectXPos;
			double absYPos = singleRelativeVertexCopy.getY() + objectYPos;

			Vector2D singleAbsoluteVertice = new Vector2D(absXPos, absYPos);

			absoluteVertices.add(singleAbsoluteVertice);
		}

		return absoluteVertices;
	}

	@Override
	public LinkedList<CollisionPoint> isIntersected(LinkedList<Line> lines,
			GameObject thisObject) {
		LinkedList<CollisionPoint> interceptPoints = new LinkedList<CollisionPoint>();

		for (Line line : lines) {
			for (Line borderLine : getBorderLines(thisObject)) {
				Vector2D interceptPoint = Geometry
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

	@Override
	public Rectangle2D.Double getBoundingBox() {
		ArrayList<Double> xValues = new ArrayList<Double>();
		ArrayList<Double> yValues = new ArrayList<Double>();
		for (Vector2D vertex : vertices) {
			xValues.add(vertex.getX());
			yValues.add(vertex.getY());
		}

		double maxX = BasicMath.max(xValues.toArray(new Double[1]));
		double maxY = BasicMath.max(yValues.toArray(new Double[1]));

		double minX = BasicMath.min(xValues.toArray(new Double[1]));
		double minY = BasicMath.min(yValues.toArray(new Double[1]));

		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public Vector2D[] getBorderPoints() {
		LinkedList<Line> borderLines = Geometry
				.getRelativeBorderLines(vertices);

		Vector2D[] movementPoints = new Vector2D[COLLISION_ACCURACY
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
			if (getVertices().isEmpty())
				return p.getVertices().isEmpty();
			return p.getVertices().equals(getVertices());
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

		Vector2D posVector = thisObject.getPositionVector();
		LinkedList<Vector2D> thisObjectAbsoluteVertices = getAbsoluteVertices(thisObject);

		// for the following calculation imagine a circle. In order to get to
		// the target angle, you have to rotate at most by 180 degrees, either
		// left or right. If the shortest rotation passes the point where the
		// angle is 0, abs(targetAngle - currAngle) must be > 180. Then we can
		// decide by the distance's sign whether we have to turn right or left.
		boolean turnLeft;
		double distance = BasicMath.findShortestDistanceModulo(
				targetRotationAngle, thisObject.getRotation(), 360);
		if (Math.abs(distance) > 180) {
			if (distance > 0) {
				turnLeft = true;
			} else {
				turnLeft = false;
			}
		} else {
			if (distance > 0) {
				turnLeft = false;
			} else {
				turnLeft = true;
			}
		}

		LinkedList<Vector2D> interceptPoints = new LinkedList<Vector2D>();
		for (GameObject anotherGameObject : gameInfo.getObjects().values()) {
			if (anotherGameObject.equals(thisObject)
					|| !anotherGameObject.isCollidable()) {
				continue;
			}

			LinkedList<Vector2D> interceptPointsWithGameObject = new LinkedList<Vector2D>();
			for (Vector2D anAbsoluteVertex : thisObjectAbsoluteVertices) {
				Circle aRotationCircle = Geometry
						.getCircleByCenterAndPointOnCircle(posVector,
								anAbsoluteVertex);

				if (anotherGameObject.getShape() instanceof Circle) {
					Pair<Vector2D, Vector2D> circleIntercepts = Geometry
							.getInterceptPointsOfCircles(
									(Circle) anotherGameObject.getShape(),
									anotherGameObject.getPositionVector(),
									aRotationCircle,
									thisObject.getPositionVector());
					if (circleIntercepts != null) {
						interceptPointsWithGameObject.add(circleIntercepts
								.getFirst());
						interceptPointsWithGameObject.add(circleIntercepts
								.getSecond());
					}
				} else {
					if (anotherGameObject.getShape() instanceof Polygon) {
						Polygon othersShape = (Polygon) anotherGameObject
								.getShape();
						for (Line anotherObjectsBorderLine : othersShape
								.getBorderLines(anotherGameObject)) {
							Vector2D[] polygonInterceptPoints = Geometry
									.getCircleLineSegmentInterceptPoints(
											aRotationCircle, posVector,
											anotherObjectsBorderLine);
							for (int i = 0; i < polygonInterceptPoints.length; i++) {
								if (polygonInterceptPoints[i] != null) {
									interceptPointsWithGameObject
											.add(polygonInterceptPoints[i]);
								}
							}
						}
					} else {
						L.severe("Can't calculate collision with objectshape "
								+ anotherGameObject.getShape().getClass()
										.getSimpleName());
						break;
					}

				}

				// Remove all intercept points, that are not on the circle
				// between
				// target angle and current angle
				LinkedList<Vector2D> copyOfInterceptPoints = new LinkedList<Vector2D>(
						interceptPointsWithGameObject);
				for (Vector2D anInterceptPoint : copyOfInterceptPoints) {

					try {
						double angle = Geometry.getAngleForPointOnCircle(
								aRotationCircle, posVector, anInterceptPoint);
						double currentAngleOfVertex = Geometry
								.getAngleForPointOnCircle(aRotationCircle,
										posVector, anAbsoluteVertex);
						if (!((turnLeft && BasicMath.isInBetweenModulo(
								BasicMath.calcModulo(currentAngleOfVertex
										+ rotationDiff, 360), angle,
								currentAngleOfVertex, 360)) || !turnLeft
								&& BasicMath.isInBetweenModulo(
										currentAngleOfVertex, angle, BasicMath
												.calcModulo(
														currentAngleOfVertex
																+ rotationDiff,
														360), 360))) {
							interceptPointsWithGameObject
									.remove(anInterceptPoint);
						}
					} catch (PointNotOnCircleException e) {
						L.warning("There is a point that is not on the circle, which shouldn't appear from previous calculation.");
						interceptPointsWithGameObject.remove(anInterceptPoint);
					}
				}

				interceptPoints.addAll(interceptPointsWithGameObject);
			}
		}

		if (interceptPoints.isEmpty()) {
			return targetRotationAngle;
		} else {

			// return thisObject.getRotation();
			// Find the point that comes first when rotating (where the angle
			// interceptpointAngle - currAngle is smallest)
			double[] angles = new double[interceptPoints.size()];
			for (int i = 0; i < interceptPoints.size(); i++) {
				angles[i] = posVector.getDistanceVectorTo(
						interceptPoints.get(i)).getAngleToXAxis();
			}
			double closestCollisionAngle = BasicMath
					.findClosestNumberModuloArray(thisObject.getRotation(),
							angles, 360);
			return closestCollisionAngle;
		}
	}
}
