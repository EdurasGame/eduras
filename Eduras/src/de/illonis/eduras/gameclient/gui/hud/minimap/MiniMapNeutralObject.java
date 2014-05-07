package de.illonis.eduras.gameclient.gui.hud.minimap;

import de.illonis.eduras.gameobjects.GameObject;

public class MiniMapNeutralObject extends MiniMapObject {

	private final GameObject object;

	public MiniMapNeutralObject(GameObject object, float x, float y, float w, float h) {
		super(object.getType(), object.getOwner(), x, y, w, h);
		this.object = object;
	}

	public GameObject getObject() {
		return object;
	}
}
