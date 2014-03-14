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
public class DynamicPolygonBlock extends GameObject {

	/**
	 * Creates a new polygon block with an empty polygon shape.
	 * 
	 * @param game
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 */
	public DynamicPolygonBlock(GameInformation game, TimingSource timingSource,
			int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.DYNAMIC_POLYGON);
		setShape(new Polygon());
	}

	/**
	 * Creates a new polygon block with initial shape data.
	 * 
	 * @param game
	 *            game information.
	 * @param source
	 *            the timing source.
	 * @param id
	 *            object id.
	 * @param vertices
	 *            vertices of polygon
	 * 
	 * @see Polygon#setVertices(Vector2D[])
	 * @see #DynamicPolygonBlock(GameInformation, TimingSource, int)
	 */
	public DynamicPolygonBlock(GameInformation game, TimingSource source,
			int id, Vector2D[] vertices) {
		this(game, source, id);
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
		((Polygon) getShape()).setVertices(vertices);
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// just block
	}

}
