package de.illonis.eduras.events;

import org.newdawn.slick.Color;

import de.illonis.eduras.gameclient.datacache.CacheInfo.TextureKey;
import de.illonis.eduras.utils.ColorUtils;

/**
 * Contains information about rendering an object.<br>
 * This may become obsolete as we implement sending the mapfile to the client.
 * 
 * @author illonis
 * 
 */
public class SetRenderInfoEvent extends ObjectEvent {

	private TextureKey texture;
	private Color color;

	/**
	 * Creates a new event that sets color and resets texture to blank.
	 * 
	 * @param id
	 *            object id.
	 * @param color
	 *            the new color
	 */
	public SetRenderInfoEvent(int id, Color color) {
		this(id, color, TextureKey.NONE);
	}

	/**
	 * Creates a new event that sets texture and resets color to gray.
	 * 
	 * @param id
	 *            object id.
	 * @param texture
	 *            the new texture
	 */
	public SetRenderInfoEvent(int id, TextureKey texture) {
		this(id, Color.gray, texture);
	}

	/**
	 * Creates a new event that sets color and texture.
	 * 
	 * @param id
	 *            object id.
	 * @param color
	 *            the new color.
	 * @param texture
	 *            the new texture.
	 */
	public SetRenderInfoEvent(int id, Color color, TextureKey texture) {
		super(GameEventNumber.SET_RENDER_INFO, id);
		this.color = color;
		this.texture = texture;
		putArgument(ColorUtils.toString(color));
		putArgument(texture.name());
	}

	/**
	 * @return the new color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the new texture.
	 */
	public TextureKey getTexture() {
		return texture;
	}

}
