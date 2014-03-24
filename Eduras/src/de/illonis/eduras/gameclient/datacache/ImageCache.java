package de.illonis.eduras.gameclient.datacache;

import java.util.HashMap;

import javax.swing.ImageIcon;

import org.newdawn.slick.Image;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;

/**
 * A cache that holds all images and shapes loaded at runtime.
 * 
 * @author illonis
 * 
 */
public final class ImageCache {

	private final static HashMap<ShapeType, Vector2df[]> shapeData = new HashMap<ShapeType, Vector2df[]>();
	private final static HashMap<ObjectType, Image> objectImages = new HashMap<ObjectType, Image>();
	private final static HashMap<ImageKey, Image> guiImages = new HashMap<ImageKey, Image>();
	private final static HashMap<ImageKey, ImageIcon> imageIcons = new HashMap<ImageKey, ImageIcon>();
	private final static HashMap<ObjectType, Image> inventoryIcons = new HashMap<ObjectType, Image>();

	static void addShape(ShapeType shapeType, Vector2df[] verts) {
		shapeData.put(shapeType, verts);
	}

	static void addImage(ObjectType objectType, Image image) {
		objectImages.put(objectType, image);
	}

	static void addImageIcon(ImageKey key, ImageIcon icon) {
		imageIcons.put(key, icon);
	}

	static void addGuiImage(ImageKey key, Image image) {
		guiImages.put(key, image);
	}

	static void addInventoryIcon(ObjectType key, Image image) {
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
	public static Vector2df[] getShapeData(ShapeType type)
			throws CacheException {
		Vector2df[] verts = shapeData.get(type);
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
	public static Image getObjectImage(ObjectType type) throws CacheException {
		Image image = objectImages.get(type);
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
	public static Image getGuiImage(ImageKey key) throws CacheException {
		Image image = guiImages.get(key);
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
	public static Image getInventoryIcon(ObjectType key)
			throws CacheException {
		Image icon = inventoryIcons.get(key);
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
