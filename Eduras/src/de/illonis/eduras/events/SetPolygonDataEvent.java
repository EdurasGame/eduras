package de.illonis.eduras.events;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.math.Vector2df;

/**
 * Event that indicates a polygon data change on a {@link DynamicPolygonObject}.
 * 
 * @author illonis
 * 
 */
public class SetPolygonDataEvent extends ObjectEvent {

	private final Vector2df[] vertices;

	/**
	 * Creates a new event of this type.
	 * 
	 * @param objectId
	 *            the id of involved gameobject.
	 * @param vertices
	 *            the new vertices.
	 */
	public SetPolygonDataEvent(int objectId, Vector2df[] vertices) {
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
	public Vector2df[] getVector2dfs() {
		return vertices;
	}

}