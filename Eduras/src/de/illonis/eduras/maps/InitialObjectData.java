package de.illonis.eduras.maps;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.Vector2D;

/**
 * Holds data for an initial object on a map.
 * 
 * @author illonis
 * 
 */
public class InitialObjectData {
	private final ObjectType type;
	private final double x, y;
	private final Vector2D[] polygonShapeVertices;

	/**
	 * Creates a new dataset holding given information for a new object.
	 * 
	 * @param type
	 *            the type of the new object.
	 * @param xPos
	 *            the x-coordinate of the new object.
	 * @param yPos
	 *            the y-coordinate of the new object.
	 */
	public InitialObjectData(ObjectType type, double xPos, double yPos) {
		this.type = type;
		this.x = xPos;
		this.y = yPos;
		polygonShapeVertices = null;
	}

	/**
	 * Creates a new dataset holding given information for a new dynamic polygon
	 * object.
	 * 
	 * @param xPos
	 *            the x-coordinate of the new object.
	 * @param yPos
	 *            the y-coordinate of the new object.
	 * @param vertices
	 *            the vertices of the dynamic polygon.
	 */
	public InitialObjectData(double xPos, double yPos, Vector2D[] vertices) {
		this.type = ObjectType.DYNAMIC_POLYGON;
		this.x = xPos;
		this.y = yPos;
		polygonShapeVertices = vertices;
	}

	/**
	 * @return the type of the object.
	 */
	public ObjectType getType() {
		return type;
	}

	/**
	 * Returns the vertices associated with this object. Will be null if the
	 * type of this object is not DYNAMIC_POLYGON.
	 * 
	 * @return The associated vertices
	 */
	public Vector2D[] getPolygonVertices() {
		return polygonShapeVertices;
	}

	/**
	 * @return the x coordinate of the object.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y coordinate of the object.
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the position as vector.
	 */
	public Vector2D getPosition() {
		return new Vector2D(x, y);
	}
}