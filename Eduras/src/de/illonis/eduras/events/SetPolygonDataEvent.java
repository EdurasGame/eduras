package de.illonis.eduras.events;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;

/**
 * Event that indicates a polygon data change on a {@link DynamicPolygonObject}.
 * 
 * @author illonis
 * 
 */
public class SetPolygonDataEvent extends ObjectEvent {

	private final Vector2f[] vertices;

	/**
	 * Creates a new event of this type.
	 * 
	 * @param objectId
	 *            the id of involved gameobject.
	 * @param vertices
	 *            the new vertices.
	 */
	public SetPolygonDataEvent(int objectId, Vector2f[] vertices) {
		super(GameEventNumber.SET_POLYGON_DATA, objectId);
		this.vertices = vertices;

		for (int i = 0; i < vertices.length; i++) {
			putArgument(vertices[i].x);
			putArgument(vertices[i].y);
		}
	}

	/**
	 * Returns the new vertices of polygon.
	 * 
	 * @return the new vertices.
	 * 
	 * @author illonis
	 */
	public Vector2f[] getVertices() {
		return vertices;
	}

}