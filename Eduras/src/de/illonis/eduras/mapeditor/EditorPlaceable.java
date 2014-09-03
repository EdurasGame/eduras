package de.illonis.eduras.mapeditor;

/**
 * Represents an object thats position can be changed in the editor.
 * 
 * @author illonis
 * 
 */
public interface EditorPlaceable extends Referencable {

	float getXPosition();

	float getYPosition();

	void setPosition(float x, float y);

	void setXPosition(float newX);

	void setYPosition(float newY);

	float getHeight();

	float getWidth();
	
	void setWidth(float width);
	
	void setHeight(float height);

}
