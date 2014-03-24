package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.Vector2df;

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
	 * @see #DynamicPolygonObject(ObjectType, GameInformation, TimingSource,
	 *      int)
	 */
	public DynamicPolygonObject(ObjectType type, GameInformation game,
			TimingSource timingSource, int id, Vector2df[] vertices) {
		this(type, game, timingSource, id);
		setPolygonVector2dfs(vertices);
	}

	/**
	 * Replaces the current polygon points by given ones
	 * 
	 * @param vertices
	 *            new vertices of shape.
	 * 
	 * 
	 * @author illonis
	 */
	public void setPolygonVector2dfs(Vector2df[] vertices) {
		// ((Polygon) getShape()).setVector2dfs(vertices);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// just block
	}

}
