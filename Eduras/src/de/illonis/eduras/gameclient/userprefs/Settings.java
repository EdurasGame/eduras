package de.illonis.eduras.gameclient.userprefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

	/**
	 * Loads settings that were saved in a previous session.
	 * 
	 * @throws FileNotFoundException
	 *             if no settings file was found.
	 */
	public void load() throws FileNotFoundException {
		Scanner scanner = new Scanner(settingsFile, ENCODING.name());
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("#"))
				continue;
			String kb[] = line.split("=");
			keyBindings.setKeyBinding(KeyBinding.valueOf(kb[0]),
					Integer.parseInt(kb[1]));
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
		writer.close();
	}

	@Override
	public void loadDefaults() {
		keyBindings.loadDefaults();
		loadBooleanDefaults();
	}

	private void loadBooleanDefaults() {
		booleanSettings.put("chooseOnPress", true);
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
}
