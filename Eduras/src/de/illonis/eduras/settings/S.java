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
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.BiggerBlock;
import de.illonis.eduras.gameobjects.Bird;
import de.illonis.eduras.items.weapons.AssaultMissile;
import de.illonis.eduras.items.weapons.AssaultRifle;
import de.illonis.eduras.items.weapons.MineMissile;
import de.illonis.eduras.items.weapons.MineWeapon;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.RocketLauncher;
import de.illonis.eduras.items.weapons.RocketMissile;
import de.illonis.eduras.items.weapons.SimpleMissile;
import de.illonis.eduras.items.weapons.SimpleWeapon;
import de.illonis.eduras.items.weapons.SniperMissile;
import de.illonis.eduras.items.weapons.SniperWeapon;
import de.illonis.eduras.items.weapons.SplashMissile;
import de.illonis.eduras.items.weapons.SplashWeapon;
import de.illonis.eduras.items.weapons.SplashedMissile;
import de.illonis.eduras.items.weapons.SwordMissile;
import de.illonis.eduras.items.weapons.SwordWeapon;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * This class holds all flexible settings.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public final class S {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	// real values
	/**
	 * Max distance of an AI object to its destination at which the target is
	 * assumed to be reached.
	 */
	public static double ai_target_reached_distance = 10;

	/**
	 * Denotes the interval in which an AI object's motion behavior is updated
	 * by the AI.
	 */
	public static long ai_motion_update_interval = 400;

	/**
	 * Denotes the width of a {@link BigBlock} object.
	 */
	public static int go_big_block_width = 20;

	/**
	 * Denotes the height of a {@link BigBlock} object.
	 */
	public static int go_big_block_height = 20;

	/**
	 * Denotes the width of a {@link BiggerBlock} object.
	 */
	public static int go_bigger_block_width = 40;

	/**
	 * Denotes the height of a {@link BiggerBlock} object.
	 */
	public static int go_bigger_block_height = 40;

	/**
	 * The speed of a {@link Bird} object.
	 */
	public static float go_bird_speed = 40;

	/**
	 * Tells whether a {@link Bird} is collidable or not.
	 */
	public static boolean go_bird_collidable = false;

	/**
	 * Max number of items a {@link PlayerMainFigure} can carry.
	 */
	public static int player_max_item_capacity = 6;

	/**
	 * Denotes the size of a {@link Missile} object.
	 */
	public static float go_missile_radius = 5;

	/**
	 * Damage caused by one {@link SimpleMissile} object.
	 */
	public static int go_simplemissile_damage = 5;

	/**
	 * Area Of Effect of a {@link SimpleMissile} object.
	 */
	public static float go_simplemissile_damage_radius = 5;

	/**
	 * Speed of a {@link SimpleMissile} object.
	 */
	public static float go_simplemissile_speed = 100;

	/**
	 * Denotes how far a {@link SimpleMissile} object can go before it
	 * disappears.
	 */
	public static float go_simplemissile_maxrange = 200;

	/**
	 * Denotes the size of a {@link SimpleWeapon} object.
	 */
	public static float go_simpleweapon_shape_radius = 10;

	/**
	 * Cooldown of a {@link SimpleWeapon}.
	 */
	public static long go_simpleweapon_cooldown = 300;

	/**
	 * Damage caused by a {@link SniperMissile}.
	 */
	public static int go_snipermissile_damage = 16;

	/**
	 * Area of Effect of a {@link SniperMissile}.
	 */
	public static float go_snipermissile_damageradius = 1;

	/**
	 * Radius of the shape of a {@link SniperMissile} object.
	 */
	public static float go_snipermissile_shape_radius = 1.5f;

	/**
	 * Speed of a {@link SniperMissile}.
	 */
	public static float go_snipermissile_speed = 500;

	/**
	 * Size (radius) of a {@link SniperWeapon} object.
	 */
	public static float go_sniperweapon_shape_radius = 5;

	/**
	 * Cooldown of {@link SniperWeapon} item.
	 */
	public static long go_sniperweapon_cooldown = 1050;

	/**
	 * Damage caused by one {@link SplashedMissile} object.
	 */
	public static int go_splashedmissile_damage = 3;

	/**
	 * Area of Effect of {@link SplashedMissile}.
	 */
	public static float go_splashedmissile_damageradius = 1;

	/**
	 * Size (radius) of {@link SplashedMissile}.
	 */
	public static float go_splashedmissile_shape_radius = 3;

	/**
	 * Speed of a {link SplashedMissile}.
	 */
	public static float go_splashedmissile_speed = 250;

	/**
	 * Damage caused by a {@link SplashMissile}.
	 */
	public static int go_splashmissile_damage = 7;

	/**
	 * Area of Effect of {@link SplashMissile}.
	 */
	public static float go_splashmissile_damageradius = 1;

	/**
	 * Size (radius) of {@link SplashMissile}.
	 */
	public static float go_splashmissile_shape_radius = 5;

	/**
	 * Speed of a {link SplashMissile}.
	 */
	public static float go_splashmissile_speed = 250;

	/**
	 * Size (radius) of a {link SplashWeapon}.
	 */
	public static float go_splashweapon_shape_radius = 7;

	/**
	 * Denotes the cooldown when you use a {@link SplashWeapon}.
	 */
	public static long go_splashweapon_cooldown = 900;

	/**
	 * Damage caused by a {@link SwordMissile}.
	 */
	public static int go_swordmissile_damage = 2;

	/**
	 * Area of Effect of {@link SwordMissile}.
	 */
	public static float go_swordmissile_damageradius = 1;

	/**
	 * Speed of {@link SwordMissile}.
	 */
	public static float go_swordmissile_speed = 100;

	/**
	 * Denotes how far a {@link SwordMissile} can go before it disappears.
	 */
	public static float go_swordmissile_maxrange = 10;

	/**
	 * Size (radius) of {@link SwordMissile}.
	 */
	public static float go_swordweapon_shape_radius = 7;

	/**
	 * Denotes the cooldown of the {@link SwordWeapon} before you can use it
	 * again.
	 */
	public static long go_swordweapon_cooldown = 100;

	/**
	 * Denotes the default respawn time of all weapons.
	 */
	public static long go_weapon_respawntime_default = 5000;

	/**
	 * Says how much health a {@link PlayerMainFigure} can have at maximum.
	 */
	public static int player_maxhealth_default = 30;

	/**
	 * Base speed of a {@link PlayerMainFigure}.
	 */
	public static float player_speed_default = 50;

	/**
	 * Tells in how many {@link SplashedMissile}s a {@link SplashMissile} breaks
	 * after hitting something.
	 */
	public static int go_splashmissile_splinters = 10;

	/**
	 * Rotation gets updated only when it has changed by an angle at least this
	 * big.
	 */
	public static double sv_performance_rotationdelta_minimum = 5;

	/**
	 * Denotes the cooldown of a {@link RocketLauncher}.
	 */
	public static long go_rocketlauncher_cooldown = 3000;

	/**
	 * Area Of Effect of a {@link RocketMissile}.
	 */
	public static float go_rocketmissile_damageradius = 100;

	/**
	 * Damage caused by a {@link RocketMissile}.
	 */
	public static int go_rocketmissile_damage = 12;

	/**
	 * Speed of a {@link RocketMissile}.
	 */
	public static float go_rocketmissile_speed = 150;

	/**
	 * Denotes how far a {@link RocketMissile} can go before it disappears.
	 */
	public static float go_rocketmissile_maxrange = 400;

	/**
	 * Size (radius) of a {@link MineWeapon}.
	 */
	public static float go_mineweapon_size = 5;

	/**
	 * Cooldown of {@link MineWeapon}.
	 */
	public static long go_mineweapon_cooldown = 500;

	/**
	 * Damage caused by a single {@link MineMissile}.
	 */
	public static int go_minemissile_damage = 6;

	/**
	 * Area Of Effect of a {@link MineMissile}.
	 */
	public static float go_minemissile_damageradius = 50;

	/**
	 * Speed of a {@link MineMissile}.
	 */
	public static float go_minemissile_speed = 0;

	/**
	 * Denotes how far a {@link MineMissile} can go before it disappears.
	 */
	public static float go_minemissile_maxrange = 1;

	/**
	 * Size (radius) of a {@link MineMissile}.
	 */
	public static float go_minemissile_shape_size = 2;

	/**
	 * Denotes by how much the {@link SplashWeapon}'s ammunition is filled up
	 * when collecting it.
	 */
	public static int go_splashweapon_fillamount = 5;

	/**
	 * Denotes the upper limit of how many bullets of a {@link SplashWeapon} can
	 * be carried.
	 */
	public static int go_splashweapon_maxammo = 50;

	/**
	 * Denotes by how much the {@link SniperWeapon}'s ammunition is filled up
	 * when collecting it.
	 */
	public static int go_sniperweapon_fillamount = 5;

	/**
	 * Denotes the upper limit of how many bullets of a {@link SniperWeapon} can
	 * be carried.
	 */
	public static int go_sniperweapon_maxammo = 20;

	/**
	 * Denotes by how much the {@link RocketLauncher}'s ammunition is filled up
	 * when collecting it.
	 */
	public static int go_rocketlauncher_fillamount = 6;

	/**
	 * Denotes the upper limit of how many bullets of a {@link RocketLauncher}
	 * can be carried.
	 */
	public static int go_rocketlauncher_maxammo = 60;

	/**
	 * Denotes by how much the {@link MineWeapon}'s ammunition is filled up when
	 * collecting it.
	 */
	public static int go_mineweapon_fillamount = 3;

	/**
	 * Denotes the upper limit of how many bullets of a {@link MineWeapon} can
	 * be carried.
	 */
	public static int go_mineweapon_maxammo = 30;

	/**
	 * Denotes by how much the {@link SimpleWeapon}'s ammunition is filled up
	 * when collecting it.
	 */
	public static int go_simpleweapon_fillamount = 25;

	/**
	 * Denotes the upper limit of how many bullets of a {@link SimpleWeapon} can
	 * be carried.
	 */
	public static int go_simpleweapon_maxammo = 200;

	/**
	 * Size (radius) of an {@link AssaultRifle}.
	 */
	public static float go_assaultrifle_shape_size = 5;

	/**
	 * Cooldown of the {@link AssaultRifle}.
	 */
	public static long go_assaultrifle_cooldown = 200;

	/**
	 * Denotes by how much the {@link AssaultRifle}'s ammunition is filled up
	 * when collecting it.
	 */
	public static int go_assaultrifle_fillamount = 50;

	/**
	 * Denotes the upper limit of how many bullets of an {@link AssaultRifle}
	 * can be carried.
	 */
	public static int go_assaultrifle_maxammo = 250;

	/**
	 * Damage caused by an {@link AssaultMissile}.
	 */
	public static int go_assaultmissile_damage = 2;

	/**
	 * Speed of an {@link AssaultMissile}.
	 */
	public static float go_assaultmissile_speed = 2000;

	/**
	 * Denotes how far an {@link AssaultMissile} can go before it disappears.
	 */
	public static float go_assaultmissile_maxrange = 4000;

	/**
	 * Size (radius) of an {@link AssaultMissile}.
	 */
	public static float go_assaultmissile_shape_size = 1;

	/**
	 * Area Of Effect of an {@link AssaultMissile}.
	 */
	public static float go_assaultmissile_damageradius = 3;

	/**
	 * Tells whether neutral objects are always shown when vision is enabled.
	 */
	public static boolean vision_neutral_always = true;

	/**
	 * Tells whether vision is disabled or not.
	 */
	public static boolean vision_disabled = true;

	public static boolean exit_on_sysout = true;

	public static long neutralbase_resource_interval = 1000;

	public static int neutralbase_resource_baseamount = 1;

	public static boolean debug_render_boundingboxes = false;

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

}
