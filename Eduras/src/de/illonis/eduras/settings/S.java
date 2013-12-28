package de.illonis.eduras.settings;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;

/**
 * This class holds all flexible settings.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public final class S {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	public static boolean myBoolean = false;

	/**
	 * Reads values from the file and writes them to the S classes attributes.
	 * 
	 * @param file
	 *            The file to read settings from.
	 * 
	 *            The file is expected to look like this:
	 * 
	 *            {@code
	 * <setting_name1>=<setting_value1>/r
	 * ...
	 * <setting_nameN>=<setting_valueN>
	 * } So each line is assumed to be a key-value pair. The values which could
	 *            be read successfully are
	 */
	public static void loadSettings(File file) {
		String[][] readSettings = parseFileToSettings(file);
		int numberOfReadSettings = readSettings.length;

		int i;
		for (i = 0; i < numberOfReadSettings; i++) {
			String settingName = readSettings[i][0];
			String settingValue = readSettings[i][1];

			Field setting;
			try {
				setting = S.class.getField(settingName);
			} catch (NoSuchFieldException | SecurityException e) {
				L.severe("Couldn't find a value for setting '" + settingName
						+ "'.");
				continue;
			}

			try {
				if (setting.getType().equals(boolean.class)) {
					setting.setBoolean(null, Boolean.parseBoolean(settingValue));
				}
			} catch (IllegalAccessException e) {
				L.log(Level.SEVERE, "Error while trying to set setting "
						+ settingName + " to value " + settingValue);
				continue;
			}
		}

	}

	private static String[][] parseFileToSettings(File file) {
		String[][] readSettings = new String[1][2];
		readSettings[0][0] = "myBoolean";
		readSettings[0][1] = "true";
		return readSettings;
	}

	public static void main(String[] args) {
		try {
			EduLog.init("test.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Myboolean vorher: " + myBoolean);

		loadSettings(null);

		System.out.println("Myboolean nachher: " + myBoolean);
	}
}
