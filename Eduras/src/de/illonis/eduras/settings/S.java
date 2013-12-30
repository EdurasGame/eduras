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
import de.illonis.eduras.unittests.GeometryUtilsTests;

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
	public static boolean myBoolean = false;
	public static int myInt = 42;
	public static long myLong = 23;
	public static String myString = "fourtytwo";
	public static float myFloat = 42.42f;
	public static double myDouble = 42.4242;

	// real values
	public static double ai_target_reached_distance = 10;
	public static long ai_motion_update_interval = 400;

	public static int go_big_block_width = 20;

	public static int go_big_block_height = 20;

	public static int go_bigger_block_width = 40;

	public static int go_bigger_block_height = 40;

	public static double go_bird_speed = 40;

	public static boolean go_bird_collidable = false;

	public static int player_max_item_capacity = 6;

	public static double go_missile_radius = 5;

	public static int go_simplemissile_damage = 5;

	public static double go_simplemissile_damage_radius = 5;

	public static double go_simplemissile_speed = 100;

	public static double go_simplemissile_maxrange = 200;

	public static double go_simpleweapon_shape_radius = 10;

	public static long go_simpleweapon_cooldown = 300;

	public static int go_snipermissile_damage = 16;

	public static double go_snipermissile_damageradius = 1.5;

	public static double go_snipermissile_shape_radius = 1.5;

	public static double go_snipermissile_speed = 500;

	public static double go_sniperweapon_shape_radius = 5;

	public static long go_sniperweapon_cooldown = 1050;

	public static int go_splashedmissile_damage = 3;

	public static double go_splashedmissile_damageradius = 1;

	public static double go_splashedmissile_shape_radius = 3;

	public static double go_splashedmissile_speed = 250;

	public static int go_splashmissile_damage = 7;

	public static double go_splashmissile_damageradius = 1;

	public static double go_splashmissile_shape_radius = 5;

	public static double go_splashmissile_speed = 250;

	public static double go_splashweapon_shape_radius = 7;

	public static long go_splashweapon_cooldown = 900;

	public static int go_swordmissile_damage = 2;

	public static double go_swordmissile_damageradius = 1;

	public static double go_swordmissile_speed = 100;

	public static double go_swordmissile_maxrange = 10;

	public static double go_swordweapon_shape_radius = 7;

	public static long go_swordweapon_cooldown = 100;

	public static long go_weapon_respawntime_default = 5000;

	public static int player_maxhealth_default = 30;

	public static double player_speed_default = 50;

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

		for (Field field : S.class.getFields()) {
			System.out.println(field.getName());
		}

		System.out.println("Myboolean vorher: " + myBoolean);
		System.out.println("MyLong vorher: " + myLong);
		System.out.println("MyInt vorher: " + myInt);
		System.out.println("MyString vorher: " + myString);
		System.out.println("MyDouble vorher: " + myDouble);
		System.out.println("MyFloat vorher: " + myFloat);

		loadSettings(new File(GeometryUtilsTests.class.getResource(
				"testsettingfile").getPath()));

		System.out.println("MyBoolean nachher: " + myBoolean);
		System.out.println("MyLong nachher: " + myLong);
		System.out.println("MyInt nachher: " + myInt);
		System.out.println("MyString nachher: " + myString);
		System.out.println("MyDouble nachher: " + myDouble);
		System.out.println("MyFloat nachher: " + myFloat);
	}
}
