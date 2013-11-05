package de.illonis.eduras.shapes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;
import de.illonis.eduras.shapes.data.ShapeParser;

/**
 * Creates shape data for shapes. If available, data are loaded from cache.
 * Otherwise they will be loaded immediately.
 * 
 * @author illonis
 * 
 */
public final class ShapeFactory {

	private final static Logger L = EduLog.getLoggerFor(ShapeFactory.class
			.getName());

	private static Vector2D[] getVerticesForShape(ShapeType shapeType) {
		try {
			return ImageCache.getShapeData(shapeType);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Error retreiving shape " + shapeType
					+ " from cache, loading directly", e);
			String shapeFile = CacheInfo.getShapeFileName(shapeType);
			try {
				Vector2D[] verts = ShapeParser.readShape(ShapeParser.class
						.getResource("data/" + shapeFile));
				return verts;
			} catch (FileCorruptException | IOException e1) {
				L.log(Level.SEVERE, "Error loading shape " + shapeType
						+ " directly.", e1);
				throw new RuntimeException(e1);
			}
		}
	}

	/**
	 * Creates a shape of given type.<br>
	 * This method uses Edruas' caching mechanisms.
	 * 
	 * @param shapeType
	 *            the type of the shape.
	 * @return the newly created shape.
	 */
	public static ObjectShape createShape(ShapeType shapeType) {

		ObjectShape s;
		switch (shapeType) {

		default:
			Vector2D[] verts = getVerticesForShape(shapeType);
			s = new Polygon(verts);
		}
		return s;
	}
}
