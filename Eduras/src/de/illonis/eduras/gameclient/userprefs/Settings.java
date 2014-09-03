package de.illonis.eduras.gameclient.userprefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.interfaces.ResettableSetting;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.utils.PathFinder;

/**
 * Holds user settings profile for current session and provides load and save
 * methods.
 * 
 * @author illonis
 * 
 */
public class Settings implements ResettableSetting {

	final static Charset ENCODING = StandardCharsets.UTF_8;

	private final static String PREF_FILE_NAME = "userprefs.dat";

	private final KeyBindings keyBindings;
	private final File settingsFile;
	private final HashMap<String, Boolean> booleanSettings;
	private final HashMap<String, Float> floatSettings;

	public static final String CONTINUOUS_ITEM_USAGE = "continuousItemUsage";
	public static final String CHOOSE_ON_PRESS = "chooseOnPress";
	public static final String MOUSE_WHEEL_SWITCH = "mouseWheelSwitch";

	public static final String SOUND_VOLUME = "soundVolume";
	public static final String MUSIC_VOLUME = "musicVolume";

	/**
	 * Creates new settings. This should be called only once each runtime and be
	 * done by {@link EdurasInitializer}.
	 * 
	 * @see #load()
	 * @see #save()
	 */
	public Settings() {
		settingsFile = new File(PathFinder.findFile(PREF_FILE_NAME));
		keyBindings = new KeyBindings();
		booleanSettings = new HashMap<String, Boolean>();
		floatSettings = new HashMap<String, Float>();
		loadDefaults();
	}

	/**
	 * Returns keybindings.
	 * 
	 * @return keybindings.
	 */
	public KeyBindings getKeyBindings() {
		return keyBindings;
	}

	private enum SettingsType {
		KEYBINDINGS, BOOLEANSETTINGS, FLOATSETTINGS, NONE
	}

	/**
	 * Loads settings that were saved in a previous session.
	 * 
	 * @throws FileNotFoundException
	 *             if no settings file was found.
	 */
	public void load() throws FileNotFoundException {
		SettingsType type = SettingsType.NONE;

		Scanner scanner = new Scanner(settingsFile, ENCODING.name());
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.equals("#keybindings")) {
				type = SettingsType.KEYBINDINGS;
				continue;
			}
			if (line.equals("#booleanoptions")) {
				type = SettingsType.BOOLEANSETTINGS;
				continue;
			}

			if (line.equals("#floatoptions")) {
				type = SettingsType.FLOATSETTINGS;
				continue;
			}
			switch (type) {
			case BOOLEANSETTINGS: {
				String bs[] = line.split("=");
				booleanSettings.put(bs[0], Boolean.parseBoolean(bs[1]));
				break;
			}
			case KEYBINDINGS: {
				String kb[] = line.split("=");
				keyBindings.setKeyBinding(KeyBinding.valueOf(kb[0]),
						Integer.parseInt(kb[1]));
				break;
			}
			case NONE:
				continue;
			case FLOATSETTINGS:
				String fs[] = line.split("=");
				floatSettings.put(fs[0], Float.parseFloat(fs[1]));
				break;
			default:
				break;
			}

		}
		scanner.close();
	}

	/**
	 * Saves settings so they can be loaded in a future session.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		PrintWriter writer = new PrintWriter(settingsFile);

		writer.println("#keybindings");
		for (KeyBinding binding : KeyBinding.values()) {
			if (!keyBindings.isDefaultKey(binding)) {
				writer.println(binding.name() + "="
						+ keyBindings.getKey(binding));
			}
		}

		writer.println("#booleanoptions");
		for (String booleanSetting : getAllBooleanSettings()) {
			writer.println(booleanSetting + "="
					+ getBooleanSetting(booleanSetting));
		}
		writer.println("#floatoptions");
		for (Entry<String, Float> floatSetting : floatSettings.entrySet()) {
			writer.println(floatSetting.getKey() + "="
					+ floatSetting.getValue());
		}
		writer.close();
	}

	@Override
	public void loadDefaults() {
		keyBindings.loadDefaults();
		loadBooleanDefaults();
		loadFloatDefaults();
	}

	private void loadFloatDefaults() {
		floatSettings.put(SOUND_VOLUME, 1f);
		floatSettings.put(MUSIC_VOLUME, 1f);
	}

	private void loadBooleanDefaults() {
		booleanSettings.put(CHOOSE_ON_PRESS, true);
		booleanSettings.put(CONTINUOUS_ITEM_USAGE, true);
		booleanSettings.put(MOUSE_WHEEL_SWITCH, true);
	}

	/**
	 * Returns the boolean setting stored under the given name.
	 * 
	 * @param settingName
	 *            The name of the setting.
	 * @return The value of the setting stored under the given name.
	 * @throws IllegalArgumentException
	 *             Thrown if the setting name doesn't exist.
	 */
	public boolean getBooleanSetting(String settingName)
			throws IllegalArgumentException {
		if (booleanSettings.containsKey(settingName)) {
			return booleanSettings.get(settingName);
		}
		throw new IllegalArgumentException("There is no setting named "
				+ settingName);
	}

	/**
	 * Returns the float setting stored under the given name.
	 * 
	 * @param settingName
	 *            The name of the setting.
	 * @return The value of the setting stored under the given name.
	 * @throws IllegalArgumentException
	 *             Thrown if the setting name doesn't exist.
	 */
	public float getFloatSetting(String settingName)
			throws IllegalArgumentException {
		if (floatSettings.containsKey(settingName)) {
			return floatSettings.get(settingName);
		}
		throw new IllegalArgumentException("There is no setting named "
				+ settingName);
	}

	/**
	 * Returns all booleansettings names.
	 * 
	 * @return A collection of names that are true/false settings.
	 */
	public Collection<String> getAllBooleanSettings() {
		return booleanSettings.keySet();
	}

	/**
	 * Returns all booleansettings names.
	 * 
	 * @return A collection of names that are true/false settings.
	 */
	public Collection<String> getAllFloatSettings() {
		return booleanSettings.keySet();
	}

	/**
	 * Set the value for the given boolean setting.
	 * 
	 * @param booleanOption
	 *            name of the setting
	 * @param b
	 *            new value
	 */
	public void setBooleanOption(String booleanOption, boolean b) {
		booleanSettings.put(booleanOption, b);
	}

	/**
	 * Set the value for the given float setting.
	 * 
	 * @param floatOption
	 *            name of the setting
	 * @param b
	 *            new value
	 */
	public void setFloatOption(String floatOption, float b) {
		floatSettings.put(floatOption, b);
	}
}
