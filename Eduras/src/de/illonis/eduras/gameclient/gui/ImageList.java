package de.illonis.eduras.gameclient.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.images.ImageFiler;

/**
 * Loads and contains all images that can be drawn by renderer.
 * 
 * @author illonis
 * 
 */
public class ImageList {

	private static final HashMap<ObjectType, BufferedImage> images = new HashMap<ObjectType, BufferedImage>();
	private static BufferedImage defaultImage;

	static {
		try {
			defaultImage = ImageFiler.load("gui/icon/noimage.png");
		} catch (IllegalArgumentException | IOException e) {
			defaultImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
		}
	}

	/**
	 * Checks if an image for given gameobject exists.
	 * 
	 * @param obj
	 *            object to look for.
	 * @return true if image exists, false otherwise.
	 */
	public static boolean hasImageFor(GameObject obj) {
		return images.containsKey(obj.getType());
	}

	/**
	 * Returns buffered image that is used for given game object.
	 * 
	 * @param obj
	 *            gameobject.
	 * @return object's image.
	 */
	public static BufferedImage getImageFor(GameObject obj) {
		if (!images.containsKey(obj.getType()))
			return defaultImage;
		return images.get(obj.getType());
	}

	/**
	 * Loads all images. This may take a lot of time so you might want to do
	 * this asynchronously.
	 */
	static void load() {
		try {
			BufferedImage i = ImageFiler.load("gui/icons/icon-weapon1.png");
			images.put(ObjectType.ITEM_WEAPON_1, i);
			BufferedImage is = ImageFiler
					.load("gui/icons/icon-weapon-sniper.png");
			images.put(ObjectType.ITEM_WEAPON_SNIPER, is);
			// TODO: load all :)
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
