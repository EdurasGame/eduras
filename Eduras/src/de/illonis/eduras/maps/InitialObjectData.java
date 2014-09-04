package de.illonis.eduras.maps;

import java.util.HashMap;

import org.newdawn.slick.Color;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.math.Vector2df;

/**
 * Holds data for an initial object on a map.
 * 
 * @author illonis
 * 
 */
public class InitialObjectData {
	private final ObjectType type;
	private final String refName;
	private final float x, y;
	private int width, height;
	private final Vector2df[] polygonShapeVector2dfs;
	private final java.util.Map<String, String> references;
	private Color color;
	private TextureKey texture;

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
		this(type, xPos, yPos, null, "");
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
		this(xPos, yPos, vertices, "");

	}

	public InitialObjectData(float xPos, float yPos, String refName) {
		this(xPos, yPos, null, refName);
	}

	public InitialObjectData(float xPos, float yPos, Vector2df[] vertices,
			String refName) {
		this(ObjectType.DYNAMIC_POLYGON_BLOCK, xPos, yPos, vertices, refName);
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
		this(type, xPos, yPos, vertices, "");
	}

	public InitialObjectData(ObjectType type, float xPos, float yPos,
			Vector2df[] vertices, String refName) {
		this.type = type;
		this.x = xPos;
		this.y = yPos;
		polygonShapeVector2dfs = vertices;
		this.refName = refName;
		references = new HashMap<String, String>();
		color = Color.gray;
		texture = TextureKey.NONE;
	}

	public InitialObjectData(ObjectType objectType, float objX, float objY,
			String currentIdentifier) {
		this(objectType, objX, objY, null, currentIdentifier);
	}

	public String getRefName() {
		return refName;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * @return the type of the object.
	 */
	public ObjectType getType() {
		return type;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
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

	public TextureKey getTexture() {
		return texture;
	}

	public void setTexture(TextureKey texture) {
		this.texture = texture;
	}

	public void addReference(String key, String reference) {
		references.put(key, reference);
	}

	public String getReference(String key) {
		return references.get(key);
	}

	/**
	 * @return the position as vector.
	 */
	public Vector2df getPosition() {
		return new Vector2df(x, y);
	}
}