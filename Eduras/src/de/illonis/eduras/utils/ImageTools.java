package de.illonis.eduras.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Provides helper methods for simple image handling.
 * 
 * @author illonis
 * 
 */
public class ImageTools {

	/**
	 * Creates a real copy of an image.
	 * 
	 * @param src
	 *            the source image.
	 * @return the copy.
	 */
	public static BufferedImage deepCopy(BufferedImage src) {
		ColorModel cm = src.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = src.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
