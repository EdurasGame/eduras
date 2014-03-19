package de.illonis.eduras.gameclient.userprefs;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.interfaces.ResettableSetting;
import de.illonis.eduras.locale.Localization;

/**
 * Maps keyboard keys to user-specific keybindings.
 * 
 * @author illonis
 * 
 */
public final class KeyBindings implements ResettableSetting {

	private final HashMap<Integer, KeyBinding> defaultKeys;
	private final HashMap<Integer, KeyBinding> keys;
	private final HashMap<KeyBinding, String> descriptions;
	private final static int[] arrs = { KeyEvent.VK_UP, KeyEvent.VK_DOWN,
			KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT };

	/**
	 * Initialize keybindings.
	 */
	public KeyBindings() {
		defaultKeys = new HashMap<Integer, KeyBinding>();
		keys = new HashMap<Integer, KeyBinding>();
		descriptions = new HashMap<KeyBinding, String>();
		init();
	}

	private void init() {
		setDefaultKeyBinding(KeyBinding.MOVE_LEFT, KeyEvent.VK_A);
		setDefaultKeyBinding(KeyBinding.MOVE_RIGHT, KeyEvent.VK_D);
		setDefaultKeyBinding(KeyBinding.MOVE_DOWN, KeyEvent.VK_S);
		setDefaultKeyBinding(KeyBinding.MOVE_UP, KeyEvent.VK_W);
		setDefaultKeyBinding(KeyBinding.ITEM_1, KeyEvent.VK_1);
		setDefaultKeyBinding(KeyBinding.ITEM_2, KeyEvent.VK_2);
		setDefaultKeyBinding(KeyBinding.ITEM_3, KeyEvent.VK_3);
		setDefaultKeyBinding(KeyBinding.ITEM_4, KeyEvent.VK_4);
		setDefaultKeyBinding(KeyBinding.ITEM_5, KeyEvent.VK_5);
		setDefaultKeyBinding(KeyBinding.ITEM_6, KeyEvent.VK_6);
		setDefaultKeyBinding(KeyBinding.CHAT, KeyEvent.VK_ENTER);
		setDefaultKeyBinding(KeyBinding.SWITCH_MODE, KeyEvent.VK_M);
		setDefaultKeyBinding(KeyBinding.EXIT_CLIENT, KeyEvent.VK_ESCAPE);
		setDefaultKeyBinding(KeyBinding.SHOW_STATS, KeyEvent.VK_TAB);
	}

	/**
	 * All keybindings that are used by game and can be assigned.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum KeyBinding {
		MOVE_LEFT, MOVE_UP, MOVE_DOWN, MOVE_RIGHT, ITEM_1, ITEM_2, ITEM_3, ITEM_4, ITEM_5, ITEM_6, EXIT_CLIENT, SHOW_STATS, SWITCH_MODE, CHAT;
	}

	/**
	 * @param binding
	 *            the binding to check.
	 * @return true if default key is assigned.
	 */
	public boolean isDefaultKey(KeyBinding binding) {
		return getKey(binding) == getDefaultKey(binding);
	}

	/**
	 * 
	 * @param binding
	 *            the binding.
	 * @return the name of a key that is assigned to a binding.
	 */
	public String getBindingString(KeyBinding binding) {
		int key = getKey(binding);
		for (int i : arrs) {
			if (i == key)
				return "Pfeil " + KeyEvent.getKeyText(key).toLowerCase();
		}
		if (key == KeyEvent.VK_UNDEFINED)
			return Localization.getString("Client.preferences.keynotbound");
		return KeyEvent.getKeyText(key);
	}

	@Override
	public void loadDefaults() {
		for (KeyBinding binding : defaultKeys.values()) {
			resetToDefault(binding);
		}
	}

	public void resetToDefault(KeyBinding binding) {
		setKeyBinding(binding, getDefaultKey(binding));
	}

	/**
	 * Sets the default key binding for a specified binding.<br>
	 * Use {@link KeyEvent#VK_UNDEFINED} to set key to <i>None</i>. This will
	 * overwrite any existing key for this binding as each binding can only have
	 * one key assigned.
	 * 
	 * @param binding
	 *            binding to set.
	 * @param key
	 *            new key, {@link KeyEvent#VK_UNDEFINED} for none.
	 */
	public void setDefaultKeyBinding(KeyBinding binding, int key) {
		defaultKeys.put(key, binding);
		// load locale description for this binding
		String localeKey = "Client.preferences.key."
				+ binding.name().toLowerCase();
		String locale = Localization.getString(localeKey);
		// do not put a description if no exists.
		if (!locale.equals("!" + localeKey + "!"))
			descriptions.put(binding, locale);
	}

	/**
	 * Sets the key binding for a specified binding.<br>
	 * Use {@link KeyEvent#VK_UNDEFINED} to set key to <i>None</i>. This will
	 * overwrite any existing key for this binding as each binding can only have
	 * one key assigned.
	 * 
	 * @param binding
	 *            binding to set.
	 * @param key
	 *            new key, {@link KeyEvent#VK_UNDEFINED} for none.
	 */
	public void setKeyBinding(KeyBinding binding, int key) {
		int oldKey = getKey(binding);
		keys.remove(oldKey);
		keys.put(key, binding);
	}

	/**
	 * Returns a description for a keybinding.
	 * 
	 * @param binding
	 *            the binding.
	 * @return a description describing this binding.
	 */
	public String getDescription(KeyBinding binding) {
		String desc = descriptions.get(binding);
		if (desc == null)
			return Localization.getStringF(
					"Client.preferences.keynotdescribed", binding.name());
		return desc;
	}

	/**
	 * Returns key that is currently assigned to given key binding.
	 * 
	 * @param binding
	 *            binding that's key is looked for.
	 * @return assigned key or {@link KeyEvent#VK_UNDEFINED} if no key is
	 *         assigned.
	 */
	public int getKey(KeyBinding binding) {
		for (int i : keys.keySet()) {
			if (keys.get(i) == binding)
				return i;
		}
		return KeyEvent.VK_UNDEFINED;
	}

	/**
	 * Returns default key for given key binding.
	 * 
	 * @param binding
	 *            binding that's key is looked for.
	 * @return default key or {@link KeyEvent#VK_UNDEFINED} if this binding does
	 *         not exist.
	 */
	public int getDefaultKey(KeyBinding binding) {
		for (int i : defaultKeys.keySet()) {
			if (defaultKeys.get(i) == binding)
				return i;
		}
		return KeyEvent.VK_UNDEFINED;
	}

	/**
	 * Returns a {@link KeyBinding} thats bound key is given key.
	 * 
	 * @param key
	 *            key to look for.
	 * @return binding assigned to given key.
	 * @throws KeyNotBoundException
	 *             if key is not assigned.
	 */
	public KeyBinding getBindingOf(int key) throws KeyNotBoundException {
		if (isBound(key)) {
			return keys.get(key);
		} else {
			throw new KeyNotBoundException(key);
		}
	}

	/**
	 * Checks whether given binding has a key assigned.
	 * 
	 * @param binding
	 *            binding to check.
	 * @return true if binding is assigned, false otherwise.
	 */
	public boolean isAssigned(KeyBinding binding) {
		return keys.values().contains(binding);
	}

	/**
	 * Checks whether given key is bound.
	 * 
	 * @param key
	 *            key to check for.
	 * @return true if key is bound, false otherwise.
	 */
	public boolean isBound(int key) {
		return (keys.get(key) != null);
	}
}
