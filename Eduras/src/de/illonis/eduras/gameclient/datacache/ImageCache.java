package de.illonis.eduras.gameclient.datacache;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;

/**
 * A cache that holds all images and shapes loaded at runtime.
 * 
 * @author illonis
 * 
 */
public final class ImageCache {

	private final static HashMap<ShapeType, Vector2D[]> shapeData = new HashMap<ShapeType, Vector2D[]>();
	private final static HashMap<ObjectType, BufferedImage> objectImages = new HashMap<ObjectType, BufferedImage>();
	private final static HashMap<ImageKey, BufferedImage> guiImages = new HashMap<ImageKey, BufferedImage>();
	private final static HashMap<ImageKey, ImageIcon> imageIcons = new HashMap<ImageKey, ImageIcon>();
	private final static HashMap<ObjectType, BufferedImage> inventoryIcons = new HashMap<ObjectType, BufferedImage>();

	static void addShape(ShapeType shapeType, Vector2D[] verts) {
		shapeData.put(shapeType, verts);
	}

	static void addImage(ObjectType objectType, BufferedImage image) {
		objectImages.put(objectType, image);
	}

	static void addImageIcon(ImageKey key, ImageIcon icon) {
		imageIcons.put(key, icon);
	}

	static void addGuiImage(ImageKey key, BufferedImage image) {
		guiImages.put(key, image);
	}

	static void addInventoryIcon(ObjectType key, BufferedImage image) {
		inventoryIcons.put(key, image);
	}

	/**
	 * Retrieves shape vertices for a specific shape from cache.
	 * 
	 * @param type
	 *            the type of the shape.
	 * @return an array of vertices.
	 * @throws CacheException
	 *             if vertices for that shape are not cached.
	 */
	public static Vector2D[] getShapeData(ShapeType type) throws CacheException {
		Vector2D[] verts = shapeData.get(type);
		if (verts == null)
			throw new CacheException("No cached data found for " + type);
		return verts;
	}

	/**
	 * Retrieves the image for an object.
	 * 
	 * @param type
	 *            type of the object
	 * @return the cached image.
	 * @throws CacheException
	 *             if imagedata for that object are not cached.
	 */
	public static BufferedImage getObjectImage(ObjectType type)
			throws CacheException {
		BufferedImage image = objectImages.get(type);
		if (image == null)
			throw new CacheException("No cached image found for " + type);
		return image;
	}

	/**
	 * Retrieves a gui-image by given key.
	 * 
	 * @param key
	 *            the key for that image.
	 * @return the cached image.
	 * @throws CacheException
	 *             if imagedata for that object are not cached.
	 */
	public static BufferedImage getGuiImage(ImageKey key) throws CacheException {
		BufferedImage image = guiImages.get(key);
		if (image == null)
			throw new CacheException("No cached image found for " + key);
		return image;
	}

	/**
	 * Retrieves an icon by given key.
	 * 
	 * @param key
	 *            the key of the icon.
	 * @return the image icon.
	 * @throws CacheException
	 *             if imagedata for that key are not cached.
	 */
	public static ImageIcon getImageIcon(ImageKey key) throws CacheException {
		ImageIcon icon = imageIcons.get(key);
		if (icon == null)
			throw new CacheException("No cached image found for " + key);
		return icon;
	}

	/**
	 * Retrieves an inventory icon by given key.
	 * 
	 * @param key
	 *            the key of the icon.
	 * @return the image icon.
	 * @throws CacheException
	 *             if imagedata for that key are not cached.
	 */
	public static BufferedImage getInventoryIcon(ObjectType key)
			throws CacheException {
		BufferedImage icon = inventoryIcons.get(key);
		if (icon == null)
			throw new CacheException("No cached inventory icon found for "
					+ key);
		return icon;
	}

	/**
	 * Clears all buffered images.
	 */
	public static void dispose() {
		// TODO: implement
	}
}
