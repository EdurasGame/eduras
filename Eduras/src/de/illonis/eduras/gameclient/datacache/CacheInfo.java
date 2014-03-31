package de.illonis.eduras.gameclient.datacache;

import java.util.HashMap;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

/**
 * Contains lists of cachable files and their location.<br>
 * Used to build the {@link ImageCache}.
 * 
 * @author illonis
 * 
 */
public final class CacheInfo {

	/**
	 * Keys for identifying cached images.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum ImageKey {
		STATISTICS_BG;
	}

	/**
	 * Absolute base url to resolve path for Slick.
	 */
	public final static String BASE_URL = "de/illonis/eduras/images/";

	private final static HashMap<ShapeType, String> shapes;
	private final static HashMap<ObjectType, String> objectImages;
	private final static HashMap<ImageKey, String> guiImages;
	private final static HashMap<ImageKey, String> imageIcons;
	private final static HashMap<ObjectType, String> inventoryIcons;

	static {
		shapes = new HashMap<ShapeType, String>();
		shapes.put(ShapeType.BIRD, "bird.esh");
		shapes.put(ShapeType.ROCKET, "rocket.esh");
		shapes.put(ShapeType.STAR, "mine.esh");

		objectImages = new HashMap<ObjectType, String>();
		inventoryIcons = new HashMap<ObjectType, String>();

		inventoryIcons.put(ObjectType.ITEM_WEAPON_SIMPLE,
				"gui/icons/icon-weapon1.png");
		inventoryIcons.put(ObjectType.ITEM_WEAPON_SNIPER,
				"gui/icons/icon-weapon-sniper.png");
		inventoryIcons.put(ObjectType.ITEM_WEAPON_SPLASH,
				"gui/icons/icon-weapon-splash.png");
		inventoryIcons.put(ObjectType.ITEM_WEAPON_SWORD,
				"gui/icons/icon-sword.png");
		inventoryIcons.put(ObjectType.ROCKETLAUNCHER,
				"gui/icons/icon-rocketlauncher.png");
		inventoryIcons.put(ObjectType.ROCKET_MISSILE,
				"gui/icons/icon-rocketlauncher.png");
		inventoryIcons.put(ObjectType.MINELAUNCHER, "gui/icons/icon-mine.png");
		inventoryIcons.put(ObjectType.ASSAULTRIFLE,
				"gui/icons/icon-assaultrifle.png");

		objectImages.put(ObjectType.BIGBLOCK, "gameobjects/bigblock.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SNIPER,
				"gameobjects/sniper.png");
		guiImages = new HashMap<ImageKey, String>();
		guiImages.put(ImageKey.STATISTICS_BG, "gui/artwork/statwindow.png");

		imageIcons = new HashMap<ImageKey, String>();
	}

	/**
	 * Retrieves the filename that contains shapedata for given shape type.
	 * 
	 * @param type
	 *            the type of the shape.
	 * @return the filename.
	 */
	public static String getShapeFileName(ShapeType type) {
		return shapes.get(type);
	}

	/**
	 * Retrieves the filename that contains image data for given object type.
	 * 
	 * @param type
	 *            type of the object.
	 * @return the file name.
	 */
	public static String getObjectImageFile(ObjectType type) {
		return objectImages.get(type);
	}

	/**
	 * Retrieves the filename for an inventory icon for given object type.
	 * 
	 * @param type
	 *            type of the object.
	 * @return the file name.
	 */
	public static String getInventoryIconFile(ObjectType type) {
		return inventoryIcons.get(type);
	}

	/**
	 * Retrieves the filename of an imageicon with given key.
	 * 
	 * @param key
	 *            key for that image
	 * @return the file name.
	 */
	public static String getIconFile(ImageKey key) {
		return imageIcons.get(key);
	}

	/**
	 * Retrieves the filename of a gui image with given key.
	 * 
	 * @param key
	 *            key for that image
	 * @return the file name.
	 */
	public static String getGuiImageFile(ImageKey key) {
		return guiImages.get(key);
	}

	static HashMap<ShapeType, String> getAllShapes() {
		return new HashMap<ShapeType, String>(shapes);
	}

	static HashMap<ObjectType, String> getAllObjectImages() {
		return new HashMap<ObjectType, String>(objectImages);
	}

	static HashMap<ImageKey, String> getAllGuiImages() {
		return new HashMap<ImageKey, String>(guiImages);
	}

	static HashMap<ImageKey, String> getAllImageIcons() {
		return new HashMap<ImageKey, String>(imageIcons);
	}

	static HashMap<ObjectType, String> getAllInventoryIcons() {
		return new HashMap<ObjectType, String>(inventoryIcons);
	}
}
