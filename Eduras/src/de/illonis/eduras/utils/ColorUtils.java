package de.illonis.eduras.utils;

import org.newdawn.slick.Color;

/**
 * Provides some functionalities for Color I/O.
 * 
 * @author illonis
 * 
 */
public class ColorUtils {

	/**
	 * Converts a color to a parseable string in format 0xAARRGGBB.<br>
	 * Conversion is done by converting each color and alpha value to
	 * hexadecimal representation.
	 * 
	 * @param color
	 *            the color to convert.
	 * @return string-representation of given color.
	 */
	public static String toString(Color color) {
		return String.format("0x%02x%02x%02x%02x", color.getAlpha(),
				color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * Parses a color from a color string.
	 * 
	 * @param color
	 *            a color in string-representation like 0xAARRGGBB.
	 * @return the color.
	 * @throws NumberFormatException
	 *             if the color string is invalid.
	 */
	public static Color fromString(String color) throws NumberFormatException {
		if (!color.startsWith("0x")) {
			throw new NumberFormatException("Color must start with \"0x\": "
					+ color);
		}
		String colorString = color.substring(2);
		int alpha = Integer.parseInt(colorString.substring(0, 2), 16);
		int r = Integer.parseInt(colorString.substring(2, 4), 16);
		int g = Integer.parseInt(colorString.substring(4, 6), 16);
		int b = Integer.parseInt(colorString.substring(6, 8), 16);
		return new Color(r, g, b, alpha);
	}
}
