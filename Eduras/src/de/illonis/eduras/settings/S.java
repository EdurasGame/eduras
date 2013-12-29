package de.illonis.eduras.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
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

	// these are testvalues only
	private static boolean myBoolean = false;
	private static int myInt = 42;
	private static long myLong = 23;
	private static String myString = "fourtytwo";
	private static float myFloat = 42.42f;
	private static double myDouble = 42.4242;

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

		if (file == null) {
			L.warning("File must not be NULL!");
			return;
		}

		LinkedList<String[]> readSettings = parseFileToSettings(file);
		int numberOfReadSettings = readSettings.size();

		int i;
		for (i = 0; i < numberOfReadSettings; i++) {
			String settingName = readSettings.get(i)[0];
			String settingValue = readSettings.get(i)[1];

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
				if (setting.getType().equals(String.class)) {
					setting.set(null, settingValue);
				}
				if (setting.getType().equals(int.class)) {
					setting.setInt(null, Integer.parseInt(settingValue));
				}
				if (setting.getType().equals(long.class)) {
					setting.setLong(null, Long.parseLong(settingValue));
				}
				if (setting.getType().equals(double.class)) {
					setting.setDouble(null, Double.parseDouble(settingValue));
				}
				if (setting.getType().equals(float.class)) {
					setting.setFloat(null, Float.parseFloat(settingValue));
				}

			} catch (IllegalAccessException e) {
				L.log(Level.SEVERE, "Error while trying to set setting "
						+ settingName + " to value " + settingValue, e);
				continue;
			} catch (IllegalArgumentException e) {
				L.log(Level.WARNING,
						"Format exception while trying to read the value "
								+ settingValue + " of setting " + settingName,
						e);
				continue;
			}
		}

	}

	private static LinkedList<String[]> parseFileToSettings(File file) {
		LinkedList<String[]> readSettings = new LinkedList<String[]>();

		BufferedReader fileReader;
		try {
			fileReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			L.log(Level.SEVERE, "Cannot find file", e1);
			return readSettings;
		}
		String readLine = "";
		try {
			while ((readLine = fileReader.readLine()) != null) {
				String[] settingNameAndValue = readLine.split("=");
				if (settingNameAndValue.length == 2) {
					readSettings.add(settingNameAndValue);
				} else {
					L.warning("Were not able to parse line " + readLine);
					continue;
				}
			}
		} catch (IOException e) {
			L.log(Level.SEVERE,
					"An IO error appeared trying to read settings file.", e);
		}

		try {
			fileReader.close();
		} catch (IOException e) {
			L.log(Level.SEVERE, "Error trying to close the file.");
		}
		return readSettings;
	}

	/**
	 * Tests the S class' fileloader.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EduLog.init("test.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Myboolean vorher: " + myBoolean);
		System.out.println("MyLong vorher: " + myLong);
		System.out.println("MyInt vorher: " + myInt);
		System.out.println("MyString vorher: " + myString);
		System.out.println("MyDouble vorher: " + myDouble);
		System.out.println("MyFloat vorher: " + myFloat);

		loadSettings(new File(
				"D:\\Reposses\\eduras\\Eduras\\src\\de\\illonis\\eduras\\unittests\\testsettingfile"));

		System.out.println("MyBoolean nachher: " + myBoolean);
		System.out.println("MyLong nachher: " + myLong);
		System.out.println("MyInt nachher: " + myInt);
		System.out.println("MyString nachher: " + myString);
		System.out.println("MyDouble nachher: " + myDouble);
		System.out.println("MyFloat nachher: " + myFloat);
	}
}
