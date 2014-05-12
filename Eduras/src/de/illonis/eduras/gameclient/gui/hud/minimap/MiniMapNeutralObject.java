package de.illonis.eduras.gameclient.gui.hud.minimap;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.GameObject;

/**
 * A neutral object on minimap.
 * 
 * @author illonis
 * 
 */
public class MiniMapNeutralObject extends MiniMapObject {

	private final GameObject object;
	private final boolean isDynamicShape;
	private final Vector2f[] vertices;

	/**
	 * Creates a static neutral object presented on the minimap.
	 * 
	 * @param object
	 *            the {@link GameObject}
	 * @param x
	 *            x position in gui.
	 * @param y
	 *            y position in gui.
	 * @param w
	 *            width.
	 * @param h
	 *            height.
	 */
	public MiniMapNeutralObject(GameObject object, float x, float y, float w,
			float h) {
		super(x, y, w, h);
		this.object = object;
		vertices = null;

		isDynamicShape = false;
	}

	/**
	 * Creates a dynamic neutral object presented on the minimap.
	 * 
	 * @param o
	 *            the {@link GameObject}
	 * @param x
	 *            x position in gui.
	 * @param y
	 *            y position in gui.
	 * @param w
	 *            width.
	 * @param h
	 *            height.
	 * @param verticesOnMinimap
	 *            vertices of the polygon in the gui
	 */
	public MiniMapNeutralObject(GameObject o, float x, float y, float w,
			float h, Vector2f[] verticesOnMinimap) {
		super(x, y, w, h);
		this.object = o;

		vertices = verticesOnMinimap;
		isDynamicShape = true;
	}

	/**
	 * @return the {@link GameObject}
	 */
	public GameObject getObject() {
		return object;
	}

	@Override
	public Vector2f getObjectLocation() {
		return object.getPositionVector();
	}

	/**
	 * Tells whether this object has a dynamic shape.
	 * 
	 * @return true if so
	 */
	public boolean isDynamicShape() {
		return isDynamicShape;
	}

	/**
	 * Returns the vertices if this object is a dynamic one or null if it's a
	 * static object.
	 * 
	 * @return vertices
	 */
	public Vector2f[] getVertices() {
		return vertices;
	}
}
