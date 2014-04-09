package de.illonis.eduras.maps;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.Vector2df;

/**
 * Holds data for an initial object on a map.
 * 
 * @author illonis
 * 
 */
public class InitialObjectData {
	private final ObjectType type;
	private final float x, y;
	private final Vector2df[] polygonShapeVector2dfs;

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
	public InitialObjectData(ObjectType type, float xPos, float yPos) {
		this.type = type;
		this.x = xPos;
		this.y = yPos;
		polygonShapeVector2dfs = null;
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
	public InitialObjectData(float xPos, float yPos, Vector2df[] vertices) {
		this.type = ObjectType.DYNAMIC_POLYGON_BLOCK;
		this.x = xPos;
		this.y = yPos;
		polygonShapeVector2dfs = vertices;
	}

	/**
	 * Creates a new dataset holding given information for a object of any type
	 * that has a polygon shape.
	 * 
	 * @param type
	 *            the type of the new object.
	 * @param xPos
	 *            the x-coordinate of the new object.
	 * @param yPos
	 *            the y-coordinate of the new object.
	 * @param vertices
	 *            the vertices of the dynamic polygon.
	 */
	public InitialObjectData(ObjectType type, float xPos, float yPos,
			Vector2df[] vertices) {
		this.type = type;
		this.x = xPos;
		this.y = yPos;
		polygonShapeVector2dfs = vertices;
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
	public Vector2df[] getPolygonVector2dfs() {
		return polygonShapeVector2dfs;
	}

	/**
	 * @return the x coordinate of the object.
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y coordinate of the object.
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the position as vector.
	 */
	public Vector2df getPosition() {
		return new Vector2df(x, y);
	}
}