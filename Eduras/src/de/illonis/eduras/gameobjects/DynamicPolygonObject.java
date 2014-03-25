package de.illonis.eduras.gameobjects;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;

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
		setShape(new Rectangle(0, 0, 0, 0));
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
			TimingSource timingSource, int id, Vector2f[] vertices) {
		this(type, game, timingSource, id);
		setPolygonVertices(vertices);
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
	public void setPolygonVertices(Vector2f[] vertices) {
		float[] points = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			points[2 * i] = vertices[i].x;
			points[2 * i + 1] = vertices[i].y;
		}
		setShape(new Polygon(points));
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// just block
	}

}
