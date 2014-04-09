package de.illonis.eduras.utils;

import java.util.Random;
import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;

/**
 * Provides some randomizing functions.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Randomizer {
	private final static Logger L = EduLog.getLoggerFor(Randomizer.class
			.getName());

	/**
	 * Gives you a random color.
	 * 
	 * @return A random color
	 */
	public static Color getRandomColor() {
		Random rand = new Random();

		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();

		return new Color(r, g, b);
	}
}
