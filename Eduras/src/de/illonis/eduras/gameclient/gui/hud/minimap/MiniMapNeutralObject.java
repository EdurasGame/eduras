package de.illonis.eduras.gameclient.gui.hud.minimap;

import de.illonis.eduras.gameobjects.GameObject;

public class MiniMapNeutralObject extends MiniMapObject {

	private final GameObject object;

	public MiniMapNeutralObject(GameObject object, float x, float y) {
		super(object.getType(), object.getOwner(), x, y, object.getShape()
				.getWidth(), object.getShape().getHeight());
		this.object = object;
	}

	public GameObject getObject() {
		return object;
	}
}
