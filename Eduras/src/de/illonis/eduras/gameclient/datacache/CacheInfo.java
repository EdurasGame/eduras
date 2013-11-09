package de.illonis.eduras.gameclient.datacache;

import java.util.HashMap;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;

/**
 * Contains lists of cachable files and their location.<br>
 * Used to build the {@link ImageCache}.
 * 
 * @author illonis
 * 
 */
public final class CacheInfo {

	private final static HashMap<ShapeType, String> shapes;
	private final static HashMap<ObjectType, String> objectImages;
	static {
		shapes = new HashMap<ShapeType, String>();
		shapes.put(ShapeType.BIRD, "bird.esh");

		objectImages = new HashMap<ObjectType, String>();
		objectImages.put(ObjectType.ITEM_WEAPON_SIMPLE,
				"gui/icons/icon-weapon1.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SNIPER,
				"gui/icons/icon-weapon-sniper.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SPLASH,
				"gui/icons/icon-weapon-splash.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SWORD,
				"gui/icons/icon-sword.png");
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

	static HashMap<ShapeType, String> getAllShapes() {
		return new HashMap<ShapeType, String>(shapes);
	}

	static HashMap<ObjectType, String> getAllObjectImages() {
		return new HashMap<ObjectType, String>(objectImages);
	}
}
