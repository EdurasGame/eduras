package de.illonis.eduras.mapeditor;

public interface EditorPlaceable {

	float getXPosition();

	float getYPosition();

	void setPosition(float x, float y);

	void setXPosition(float newX);

	void setYPosition(float newY);

	float getHeight();

	float getWidth();

}
