package de.illonis.eduras.gameclient.userprefs;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.newdawn.slick.Input;

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
		setDefaultKeyBinding(KeyBinding.MOVE_LEFT, Input.KEY_A);
		setDefaultKeyBinding(KeyBinding.MOVE_RIGHT, Input.KEY_D);
		setDefaultKeyBinding(KeyBinding.MOVE_DOWN, Input.KEY_S);
		setDefaultKeyBinding(KeyBinding.MOVE_UP, Input.KEY_W);
		setDefaultKeyBinding(KeyBinding.ITEM_1, Input.KEY_1);
		setDefaultKeyBinding(KeyBinding.ITEM_2, Input.KEY_2);
		setDefaultKeyBinding(KeyBinding.ITEM_3, Input.KEY_3);
		setDefaultKeyBinding(KeyBinding.ITEM_4, Input.KEY_4);
		setDefaultKeyBinding(KeyBinding.ITEM_5, Input.KEY_5);
		setDefaultKeyBinding(KeyBinding.ITEM_6, Input.KEY_6);
		setDefaultKeyBinding(KeyBinding.ITEM_7, Input.KEY_7);
		setDefaultKeyBinding(KeyBinding.ITEM_8, Input.KEY_8);
		setDefaultKeyBinding(KeyBinding.ITEM_9, Input.KEY_9);
		setDefaultKeyBinding(KeyBinding.ITEM_10, Input.KEY_0);
		setDefaultKeyBinding(KeyBinding.PAGE_UP, Input.KEY_Q);
		setDefaultKeyBinding(KeyBinding.CHAT, Input.KEY_ENTER);
		setDefaultKeyBinding(KeyBinding.SWITCH_MODE, Input.KEY_M);
		setDefaultKeyBinding(KeyBinding.EXIT_CLIENT, Input.KEY_ESCAPE);
		setDefaultKeyBinding(KeyBinding.SHOW_STATS, Input.KEY_TAB);
	}

	/**
	 * All keybindings that are used by game and can be assigned.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum KeyBinding {
		MOVE_LEFT, MOVE_UP, MOVE_DOWN, MOVE_RIGHT, ITEM_1, ITEM_2, ITEM_3, ITEM_4, ITEM_5, ITEM_6, ITEM_7, ITEM_8, ITEM_9, ITEM_10, EXIT_CLIENT, SHOW_STATS, SWITCH_MODE, CHAT, PAGE_UP;
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
		if (key == Input.KEY_UNLABELED)
			return Localization.getString("Client.preferences.keynotbound");
		return Input.getKeyName(key);
	}

	@Override
	public void loadDefaults() {
		for (KeyBinding binding : defaultKeys.values()) {
			resetToDefault(binding);
		}
	}

	/**
	 * Resets a keybinding to default key.
	 * 
	 * @param binding
	 *            the binding to reset.
	 */
	public void resetToDefault(KeyBinding binding) {
		setKeyBinding(binding, getDefaultKey(binding));
	}

	/**
	 * Sets the default key binding for a specified binding.<br>
	 * Use {@link Input#KEY_UNLABELED} to set key to <i>None</i>. This will
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
		return Input.KEY_UNLABELED;
	}

	/**
	 * Returns default key for given key binding.
	 * 
	 * @param binding
	 *            binding that's key is looked for.
	 * @return default key or {@link Input#KEY_UNLABELED} if this binding does
	 *         not exist.
	 */
	public int getDefaultKey(KeyBinding binding) {
		for (int i : defaultKeys.keySet()) {
			if (defaultKeys.get(i) == binding)
				return i;
		}
		return Input.KEY_UNLABELED;
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
