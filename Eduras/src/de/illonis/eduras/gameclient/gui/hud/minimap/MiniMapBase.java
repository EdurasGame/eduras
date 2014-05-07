package de.illonis.eduras.gameclient.gui.hud.minimap;

import org.newdawn.slick.Color;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.NeutralBase;

public class MiniMapBase extends MiniMapObject {

	private final NeutralBase base;

	public MiniMapBase(NeutralBase base, float x, float y, float w, float h) {
		super(ObjectType.NEUTRAL_BASE, base.getOwner(), x, y, w, h);
		this.base = base;
	}
	
	public Color getColor() {
		if (base.getCurrentOwnerTeam() == null)
			return Color.white;
		return base.getCurrentOwnerTeam().getColor();
	}

}
