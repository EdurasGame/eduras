package de.illonis.eduras.shapes.data;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.EditablePolygon;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapecreator.ShapeFiler;

/**
 * Provides functionality to parse a human readable shapefile into shape data.
 * More details on readable shapefiles can be found in docbook in the
 * shapecreator section.
 * 
 * @author illonis
 * 
 */
public class ShapeParser {

	/**
	 * The default file extension for saved maps.<br>
	 * erm stands for "eduras? readable mapfile".
	 */
	public final static String FILE_EXTENSION = ShapeFiler.FILE_EXT;

	/**
	 * Reads a textfile containing shape data.
	 * 
	 * @param inputFile
	 *            the input file. Must have correct syntax as described in
	 *            docbook. It's file extension must be correct (
	 *            {@value #FILE_EXTENSION}).
	 * @return the shape data.
	 * @throws FileCorruptException
	 *             if file contains invalid data or is not well formatted.
	 * @throws IOException
	 *             if an I/O error occurs while reading the file.
	 */
	public static Vector2df[] readShape(URL inputFile)
			throws FileCorruptException, IOException {

		EditablePolygon poly = ShapeFiler.loadShape(inputFile);

		LinkedList<Vector2df> vertices = new LinkedList<Vector2df>();
		for (Vector2df vertice : poly.getVector2dfs()) {
			vertices.add(new Vector2df(vertice.getX(), vertice.getY()));
		}
		return vertices.toArray(new Vector2df[] {});
	}
}
