package de.illonis.eduras.events;

import de.illonis.eduras.gameobjects.DynamicPolygonBlock;
import de.illonis.eduras.math.Vector2D;

/**
 * Event that indicates a polygon data change on a {@link DynamicPolygonBlock}.
 * 
 * @author illonis
 * 
 */
public class SetPolygonDataEvent extends ObjectEvent {

	private final Vector2D[] vertices;

	/**
	 * Creates a new event of this type.
	 * 
	 * @param objectId
	 *            the id of involved gameobject.
	 * @param vertices
	 *            the new vertices.
	 */
	public SetPolygonDataEvent(int objectId, Vector2D[] vertices) {
		super(GameEventNumber.SET_POLYGON_DATA, objectId);
		this.vertices = vertices;

		for (int i = 0; i < vertices.length; i++) {
			putArgument(vertices[i].getX());
			putArgument(vertices[i].getY());
		}
	}

	/**
	 * Returns the new vertices of polygon.
	 * 
	 * @return the new vertices.
	 * 
	 * @author illonis
	 */
	public Vector2D[] getVertices() {
		return vertices;
	}

}