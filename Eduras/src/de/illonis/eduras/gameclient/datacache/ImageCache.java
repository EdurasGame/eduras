package de.illonis.eduras.gameclient.datacache;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.CacheInfo.TextureKey;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

/**
 * A cache that holds all images and shapes loaded at runtime.
 * 
 * @author illonis
 * 
 */
public final class ImageCache {

	private final static HashMap<ShapeType, Vector2f[]> shapeData = new HashMap<ShapeType, Vector2f[]>();
	private final static HashMap<ObjectType, Image> objectImages = new HashMap<ObjectType, Image>();
	private final static HashMap<ImageKey, Image> guiImages = new HashMap<ImageKey, Image>();
	private final static HashMap<ObjectType, Image> inventoryIcons = new HashMap<ObjectType, Image>();
	private final static HashMap<TextureKey, Image> textures = new HashMap<TextureKey, Image>();

	static void addShape(ShapeType shapeType, Vector2f[] verts) {
		shapeData.put(shapeType, verts);
	}

	static void addTexture(TextureKey textureType, Image texture) {
		textures.put(textureType, texture);
	}

	static void addImage(ObjectType objectType, Image image) {
		objectImages.put(objectType, image);
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
	public static Vector2f[] getShapeData(ShapeType type) throws CacheException {
		Vector2f[] verts = shapeData.get(type);
		if (verts == null)
			throw new CacheException("No cached data found for " + type);
		return verts;
	}

	/**
	 * Retrieves a texture of a specific type from cache.
	 * 
	 * @param type
	 *            the type of the texture.
	 * @return the texture.
	 * @throws CacheException
	 *             if this texture is not cached.
	 */
	public static Image getTexture(TextureKey type) throws CacheException {
		if (type == TextureKey.NONE)
			throw new CacheException("No texture assigned.");
		Image image = textures.get(type);
		if (image == null)
			throw new CacheException("No cached texture found for " + type);
		return image;
	}

	/**
	 * Retrieves the image for an object.
	 * 
	 * @param o
	 *            the game object
	 * @return the cached image.
	 * @throws CacheException
	 *             if imagedata for that object are not cached.
	 */
	public static Image getObjectImage(GameObject o) throws CacheException {
		if (isTextured(o)) {
			return getTexture(objectToTexture(o));
		}
		Image image = objectImages.get(o.getType());
		if (image == null)
			throw new CacheException("No cached image found for " + o.getType());
		return image;
	}

	/**
	 * Retrieves the appropriate texture key for given game object. This returns
	 * null if {@link #isTextured(GameObject)} returns false.
	 * 
	 * @param o
	 *            the game object.
	 * @return the texture key or null if none assigned.
	 */
	public static TextureKey objectToTexture(GameObject o) {
		switch (o.getType()) {
		case NEUTRAL_BASE:
			Base base = (Base) o;
			if (base.getCurrentOwnerTeam() == null) {
				return TextureKey.BASE;
			} else {
				if (base.getCurrentOwnerTeam().getColor().r > 0.5f) {
					return TextureKey.BASE_RED;
				} else
					return TextureKey.BASE_BLUE;
			}
		case PORTAL:
			return TextureKey.PORTAL;
		case DYNAMIC_POLYGON_BLOCK:
			return o.getTexture();
		case BIGBLOCK:
		case BIGGERBLOCK:
			return TextureKey.ROOF;
		default:
			return null;
		}
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
	 * Retrieves an inventory icon by given key.
	 * 
	 * @param key
	 *            the key of the icon.
	 * @return the image icon.
	 * @throws CacheException
	 *             if imagedata for that key are not cached.
	 */
	public static Image getInventoryIcon(ObjectType key) throws CacheException {
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
		inventoryIcons.clear();
		shapeData.clear();
		objectImages.clear();
		guiImages.clear();
	}

	/**
	 * Checks if the image returned by {@link #getObjectImage(GameObject)} for
	 * given type is a texture or not.
	 * 
	 * @param o
	 *            the game object.
	 * @return true if the image returned by {@link #getObjectImage(GameObject)}
	 *         is a texture for this type, false otherwise.
	 */
	public static boolean isTextured(GameObject o) {
		switch (o.getType()) {
		case PORTAL:
		case DYNAMIC_POLYGON_BLOCK:
		case BIGBLOCK:
		case BIGGERBLOCK:
		case NEUTRAL_BASE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Checks if the texture for this object should fit.
	 * 
	 * @param o
	 *            the game object.
	 * @return true if the texture should fit, false otherwise.
	 */
	public static boolean shouldFit(GameObject o) {
		switch (o.getType()) {
		case PORTAL:
			return true;
		default:
			return false;
		}
	}
}
