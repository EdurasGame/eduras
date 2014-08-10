package de.illonis.eduras.gameclient.gui.hud.minimap;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.units.PlayerMainFigure;

/**
 * The player representation on minimap.
 * 
 * @author illonis
 * 
 */
public class MiniMapPlayer extends MiniMapObject {

	private final PlayerMainFigure player;

	/**
	 * @param player
	 *            the {@link PlayerMainFigure}.
	 * @param x
	 *            x position in gui.
	 * @param y
	 *            y position in gui.
	 * @param w
	 *            width.
	 * @param h
	 *            height.
	 */
	public MiniMapPlayer(PlayerMainFigure player, float x, float y, float w,
			float h) {
		super(x, y, w, h);
		this.player = player;
	}

	/**
	 * @return the {@link PlayerMainFigure}.
	 */
	public PlayerMainFigure getPlayer() {
		return player;
	}

	/**
	 * @return the color of players team.
	 */
	public Color getColor() {
		if (player == null || player.getTeam() == null)
			return Color.white;
		return player.getTeam().getColor();
	}

	@Override
	public Vector2f getObjectLocation() {

		return player.getPositionVector();
	}

}
