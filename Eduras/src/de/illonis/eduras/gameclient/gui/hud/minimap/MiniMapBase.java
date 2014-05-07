package de.illonis.eduras.gameclient.gui.hud.minimap;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameobjects.NeutralBase;

/**
 * The minimap representation of a base.
 * 
 * @author illonis
 * 
 */
public class MiniMapBase extends MiniMapObject {

	private final NeutralBase base;

	/**
	 * @param base
	 *            the {@link NeutralBase}
	 * @param x
	 *            x position in gui.
	 * @param y
	 *            y position in gui.
	 * @param w
	 *            width.
	 * @param h
	 *            height.
	 */
	public MiniMapBase(NeutralBase base, float x, float y, float w, float h) {
		super(x, y, w, h);
		this.base = base;
	}

	/**
	 * @return the color of the owning team or white.
	 */
	public Color getColor() {
		if (base.getCurrentOwnerTeam() == null)
			return Color.white;
		return base.getCurrentOwnerTeam().getColor();
	}
	
	@Override
	public Vector2f getObjectLocation() {
		return base.getPositionVector();
	}

}
