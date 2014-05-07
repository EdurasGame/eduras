package de.illonis.eduras.gameclient.gui.hud.minimap;

import de.illonis.eduras.ObjectFactory.ObjectType;

public abstract class MiniMapObject {

	private final ObjectType type;
	private final int owner;
	private float x;
	private float y;
	private float width;
	private float height;

	public MiniMapObject(ObjectType type, int owner, float x, float y, float width, float height) {
		this.type = type;
		this.owner = owner;
		this.width = width;
		this.height = height;
		setLocation(x, y);
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}

}
