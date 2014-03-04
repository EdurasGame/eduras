package de.illonis.eduras.settings;

import de.illonis.eduras.exceptions.UserDataMissingException;
import de.illonis.eduras.interfaces.ResettableSetting;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * Holds user settings profile for current session and provides load and save
 * methods.
 * 
 * @author illonis
 * 
 */
public class Settings implements ResettableSetting {

	private KeyBindings keyBindings;

	/**
	 * Creates new settings. This should be called only once each runtime and be
	 * done by {@link EdurasInitializer}.
	 * 
	 * @see #load()
	 * @see #save()
	 */
	public Settings() {
		keyBindings = new KeyBindings();
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
	 * @throws UserDataMissingException
	 *             if no valid previous session was found.
	 */
	public void load() throws UserDataMissingException {

	}

	/**
	 * Saves settings so they can be loaded in a future session.
	 */
	public void save() {

	}

	@Override
	public void loadDefaults() {
		keyBindings.loadDefaults();
	}
}
