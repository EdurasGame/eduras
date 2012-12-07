package de.illonis.eduras.gui.renderer;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * Loads and contains all images that can be drawn by renderer.
 * 
 * @author illonis
 * 
 */
public class ImageList {

	private HashMap<ObjectType, BufferedImage> images;

	ImageList() {
		images = new HashMap<ObjectType, BufferedImage>();
	}

	/**
	 * Checks if an image for given gameobject exists.
	 * 
	 * @param obj
	 *            object to look for.
	 * @return true if image exists, false otherwise.
	 */
	public boolean hasImageFor(GameObject obj) {
		return images.containsKey(obj.getType());
	}

	/**
	 * Loads all images. This may take a lot of time so you might want to do
	 * this asynchronously.
	 */
	void load() {

	}
}
