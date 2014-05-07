package de.illonis.eduras.gameclient.gui.hud.minimap;

import org.newdawn.slick.Color;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.NeutralBase;

public class MiniMapBase extends MiniMapObject {

	private final NeutralBase base;

	public MiniMapBase(NeutralBase base, float x, float y) {
		super(ObjectType.NEUTRAL_BASE, base.getOwner(), x, y, base.getShape()
				.getWidth(), base.getShape().getHeight());
		this.base = base;
	}
	
	public Color getColor() {
		if (base.getCurrentOwnerTeam() == null)
			return Color.white;
		return base.getCurrentOwnerTeam().getColor();
	}

}
