package de.illonis.eduras.gameclient.gui.hud.minimap;

import org.newdawn.slick.Color;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.units.PlayerMainFigure;

public class MiniMapPlayer extends MiniMapObject {
	
	private final PlayerMainFigure player;

	public MiniMapPlayer(PlayerMainFigure player, float x, float y, float w, float h) {
		super(ObjectType.PLAYER, player.getOwner(), x, y, w, h);
		this.player = player;
	}
	
	public PlayerMainFigure getPlayer() {
		return player;
	}

	public Color getColor() {
		return player.getTeam().getColor();
	}

}
