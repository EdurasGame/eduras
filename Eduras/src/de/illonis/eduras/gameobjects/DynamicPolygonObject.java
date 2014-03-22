package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Polygon;

/**
 * A polygon block that's shape is designed to be changed later on, for example
 * via events from server.
 * 
 * @author illonis
 * 
 */
public class DynamicPolygonObject extends GameObject {

	/**
	 * Creates a new polygon block with an empty polygon shape.
	 * 
	 * @param type
	 *            The type of the dynamic polygon
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public DynamicPolygonObject(ObjectType type, GameInformation game,
			TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setObjectType(type);
	}

	/**
	 * Creates a new polygon block with initial shape data.
	 * 
	 * @param type
	 *            The type of the polygon object.
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 * @param vertices
	 *            vertices of polygon
	 * 
	 * @see Polygon#setVertices(Vector2D[])
	 * @see #DynamicPolygonObject(ObjectType, GameInformation, TimingSource,
	 *      int)
	 */
	public DynamicPolygonObject(ObjectType type, GameInformation game,
			TimingSource timingSource, int id, Vector2D[] vertices) {
		this(type, game, timingSource, id);
		setPolygonVertices(vertices);
	}

	/**
	 * Replaces the current polygon points by given ones
	 * 
	 * @param vertices
	 *            new vertices of shape.
	 * 
	 * @see Polygon#setVertices(Vector2D[])
	 * 
	 * @author illonis
	 */
	public void setPolygonVertices(Vector2D[] vertices) {
		//((Polygon) getShape()).setVertices(vertices);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// just block
	}

}
