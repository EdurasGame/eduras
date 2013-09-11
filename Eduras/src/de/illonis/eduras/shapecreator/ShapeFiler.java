package de.illonis.eduras.shapecreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Handle loading and saving of shapes.
 * 
 * @author illonis
 * 
 */
public class ShapeFiler {
	/**
	 * File extension for saved shapes.
	 */
	public final static String FILE_EXT = "esh";

	/**
	 * Loads a shape from given file.<br>
	 * File must have the {@value #FILE_EXT} extension.
	 * 
	 * @param inputFile
	 *            the input file.
	 * @return the editable polygon saved in the file.
	 * @throws FileCorruptException
	 *             if file could not be parsed correctly.
	 * @throws FileNotFoundException
	 *             if target file does not exist.
	 * @throws IOException
	 *             if a I/O error occurs while reading the file.
	 * 
	 * @author illonis
	 */
	public static EditablePolygon loadShape(File inputFile)
			throws FileCorruptException, FileNotFoundException, IOException {

		return new EditablePolygon();
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

		outputFile.createNewFile();
	}
}
