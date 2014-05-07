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

	/**
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
}
