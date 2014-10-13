package de.illonis.eduras.unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.newdawn.slick.Color;

import de.illonis.eduras.utils.ColorUtils;

/**
 * Tests the {@link ColorUtils} class.
 * 
 * @author illonis
 * 
 */
public class ColorUtilTest {

	/**
	 * Tests {@link ColorUtils#toString(Color)} and
	 * {@link ColorUtils#fromString(String)} for a wide range of colors.
	 */
	@Test
	public void colorConversion() {
		for (int r = 0; r < 256; r += 15) {
			for (int g = 0; g < 256; g += 15) {
				for (int b = 0; b < 256; b += 15) {
					for (int a = 0; a < 256; a += 15) {
						Color color = new Color(r, g, b, a);
						assertEquals(color, ColorUtils.fromString(ColorUtils
								.toString(color)));
					}
				}
			}
		}
	}
}
