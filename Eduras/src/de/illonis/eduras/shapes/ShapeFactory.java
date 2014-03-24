package de.illonis.eduras.shapes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.math.Vector2df;
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

	private static Vector2df[] getVector2dfsForShape(ShapeType shapeType) {
		try {
			return ImageCache.getShapeData(shapeType);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Error retrieving shape " + shapeType
					+ " from cache, loading directly", e);
			String shapeFile = CacheInfo.getShapeFileName(shapeType);
			try {
				Vector2df[] verts = ShapeParser.readShape(ShapeParser.class
						.getResource(shapeFile));
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
	public static Shape createShape(ShapeType shapeType) {

		Shape s;
		switch (shapeType) {

		default:
			Vector2df[] verts = getVector2dfsForShape(shapeType);
			float points[] = new float[verts.length * 2];

			for (int i = 0; i < verts.length; i++) {
				points[2 * i] = (float) verts[i].getX();
				points[2 * i + 1] = (float) verts[i].getY();
			}
			s = new Polygon(points);
		}
		return s;
	}
}
