package de.illonis.eduras.shapecreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Handle loading and saving of shapes.<br>
 * Shape files must have a correct syntax as described in docbook and use the
 * file extension {@value #FILE_EXT}.
 * 
 * @author illonis
 * 
 */
public class ShapeFiler {
	/**
	 * File extension for saved shapes.
	 */
	public final static String FILE_EXT = "esh";
	private final static String ENCODING = "UTF-8";

	/**
	 * Loads a shape from given file.
	 * 
	 * @param inputFile
	 *            url locating the input file.
	 * @return the editable polygon saved in the file.
	 * @throws FileCorruptException
	 *             if file could not be parsed correctly.
	 * @throws IOException
	 *             if a I/O error occurs while reading the file.
	 * 
	 * @author illonis
	 */
	public static EditablePolygon loadShape(URL inputFile)
			throws FileCorruptException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputFile.openStream(), ENCODING));

		String line = null;
		EditablePolygon polygon = new EditablePolygon();
		while ((line = reader.readLine()) != null) {
			if (line.trim().isEmpty())
				continue;
			if (line.startsWith("#"))
				continue;
			String[] values = line.split(",");
			try {
				double x = Double.parseDouble(values[0].trim());
				double y = Double.parseDouble(values[1].trim());

				polygon.addVertice(new Vertice(x, y));
			} catch (NumberFormatException e) {
				throw new FileCorruptException(inputFile.toString());
			}
		}
		reader.close();
		return polygon;
	}

	/**
	 * Saves a shape to given file.
	 * 
	 * @param shape
	 *            the shape that should be saved.
	 * @param outputFile
	 *            target file.
	 * @throws IOException
	 *             if a I/O error occurs while writing to the file.
	 * 
	 * @author illonis
	 */
	public static void saveShape(EditablePolygon shape, File outputFile)
			throws IOException {

		BufferedWriter writer = new BufferedWriter(new PrintWriter(outputFile,
				ENCODING));

		for (Vertice v : shape.getVertices()) {
			writer.write(v.getX() + "," + v.getY());
			writer.newLine();
		}
		writer.close();

	}
}
