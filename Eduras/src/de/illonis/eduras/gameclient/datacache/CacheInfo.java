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
