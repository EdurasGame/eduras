package de.illonis.eduras.gameclient.userprefs;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.newdawn.slick.Input;

import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.interfaces.ResettableSetting;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.units.InteractMode;

/**
 * Maps keyboard keys to user-specific keybindings.
 * 
 * @author illonis
 * 
 */
public final class KeyBindings implements ResettableSetting {

	private final HashMap<KeyBinding, Integer> defaultKeys;
	private final HashMap<InteractMode, HashMap<Integer, KeyBinding>> keys;
	private final HashMap<KeyBinding, String> descriptions;

	/**
	 * Initialize keybindings.
	 */
	public KeyBindings() {
		defaultKeys = new HashMap<KeyBinding, Integer>();
		keys = new HashMap<InteractMode, HashMap<Integer, KeyBinding>>();
		for (InteractMode mode : InteractMode.values()) {
			keys.put(mode, new HashMap<Integer, KeyBinding>());
		}
		descriptions = new HashMap<KeyBinding, String>();
		init();
	}

	private void init() {
		// ego mode
		setDefaultKeyBinding(KeyBinding.MOVE_LEFT, Input.KEY_A);
		setDefaultKeyBinding(KeyBinding.MOVE_RIGHT, Input.KEY_D);
		setDefaultKeyBinding(KeyBinding.MOVE_DOWN, Input.KEY_S);
		setDefaultKeyBinding(KeyBinding.MOVE_UP, Input.KEY_W);
		setDefaultKeyBinding(KeyBinding.ITEM_ASSAULT, Input.KEY_1);
		setDefaultKeyBinding(KeyBinding.ITEM_SWORD, Input.KEY_2);
		setDefaultKeyBinding(KeyBinding.ITEM_SIMPLE, Input.KEY_3);
		setDefaultKeyBinding(KeyBinding.ITEM_SNIPER, Input.KEY_4);
		setDefaultKeyBinding(KeyBinding.ITEM_ROCKET, Input.KEY_5);
		setDefaultKeyBinding(KeyBinding.ITEM_SPLASH, Input.KEY_6);
		setDefaultKeyBinding(KeyBinding.ITEM_MINE, Input.KEY_7);
		setDefaultKeyBinding(KeyBinding.BLINK, Input.KEY_SPACE);

		// strategy mode
		setDefaultKeyBinding(KeyBinding.PAGE_UP, Input.KEY_Q);
		setDefaultKeyBinding(KeyBinding.SCROLL_LEFT, Input.KEY_A);
		setDefaultKeyBinding(KeyBinding.SCROLL_RIGHT, Input.KEY_D);
		setDefaultKeyBinding(KeyBinding.SCROLL_DOWN, Input.KEY_S);
		setDefaultKeyBinding(KeyBinding.SCROLL_UP, Input.KEY_W);
		setDefaultKeyBinding(KeyBinding.STRATEGY_1, Input.KEY_1);
		setDefaultKeyBinding(KeyBinding.STRATEGY_2, Input.KEY_2);
		setDefaultKeyBinding(KeyBinding.STRATEGY_3, Input.KEY_3);
		setDefaultKeyBinding(KeyBinding.STRATEGY_4, Input.KEY_4);
		setDefaultKeyBinding(KeyBinding.STRATEGY_5, Input.KEY_5);
		setDefaultKeyBinding(KeyBinding.STRATEGY_6, Input.KEY_6);
		setDefaultKeyBinding(KeyBinding.STRATEGY_7, Input.KEY_7);
		setDefaultKeyBinding(KeyBinding.STRATEGY_8, Input.KEY_8);
		setDefaultKeyBinding(KeyBinding.STRATEGY_9, Input.KEY_9);
		setDefaultKeyBinding(KeyBinding.STRATEGY_10, Input.KEY_0);

		// general bindings
		setDefaultKeyBinding(KeyBinding.CHAT, Input.KEY_ENTER);
		setDefaultKeyBinding(KeyBinding.SWITCH_MODE, Input.KEY_E);
		setDefaultKeyBinding(KeyBinding.CANCEL, Input.KEY_ESCAPE);
		setDefaultKeyBinding(KeyBinding.SHOW_STATS, Input.KEY_TAB);
		setDefaultKeyBinding(KeyBinding.SELECT_TEAM, Input.KEY_P);
	}

	/**
	 * All keybindings that are used by game and can be assigned.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum KeyBinding {
		MOVE_LEFT, MOVE_UP, MOVE_DOWN, MOVE_RIGHT, ITEM_ASSAULT, ITEM_SWORD,
		ITEM_SNIPER, ITEM_SPLASH, ITEM_SIMPLE, ITEM_MINE, ITEM_ROCKET, CANCEL,
		SHOW_STATS, SWITCH_MODE, CHAT, PAGE_UP, BLINK, SELECT_TEAM,
		SCROLL_LEFT, SCROLL_RIGHT, SCROLL_DOWN, SCROLL_UP, STRATEGY_1,
		STRATEGY_2, STRATEGY_3, STRATEGY_4, STRATEGY_5, STRATEGY_6, STRATEGY_7,
		STRATEGY_8, STRATEGY_9, STRATEGY_10;
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
		for (KeyBinding binding : defaultKeys.keySet()) {
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
		defaultKeys.put(binding, key);
		// load locale description for this binding
		String localeKey = "Client.preferences.key."
				+ binding.name().toLowerCase();
		String locale = Localization.getString(localeKey);
		// do not put a description if no exists.
		if (!locale.equals("!" + localeKey + "!"))
			descriptions.put(binding, locale);
	}

	/**
	 * Sets the key binding for a specified binding in given mode.<br>
	 * Use {@link KeyEvent#VK_UNDEFINED} to set key to <i>None</i>. This will
	 * overwrite any existing key for this binding in given mode as each binding
	 * can only have one key assigned in each interact mode.
	 * 
	 * @param binding
	 *            binding to set.
	 * @param key
	 *            new key, {@link KeyEvent#VK_UNDEFINED} for none.
	 */
	public void setKeyBinding(KeyBinding binding, int key) {
		InteractMode mode = InteractModeKeys.getModeOfBinding(binding);
		keys.get(mode).put(key, binding);
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
	 * Returns key that is currently assigned to given key binding in an
	 * interactmode.
	 * 
	 * @param binding
	 *            binding that's key is looked for.
	 * @return assigned key or {@link KeyEvent#VK_UNDEFINED} if no key is
	 *         assigned.
	 */
	public int getKey(KeyBinding binding) {
		InteractMode mode = InteractModeKeys.getModeOfBinding(binding);
		HashMap<Integer, KeyBinding> modeBindings = keys.get(mode);
		for (int i : modeBindings.keySet()) {
			if (modeBindings.get(i) == binding)
				return i;
		}
		return Input.KEY_UNLABELED;
	}

	/**
	 * Returns default key for given key binding in given mode.
	 * 
	 * @param binding
	 *            binding that's key is looked for.
	 * @return default key or {@link Input#KEY_UNLABELED} if this binding does
	 *         not exist.
	 */
	public int getDefaultKey(KeyBinding binding) {
		if (!defaultKeys.containsKey(binding)) {
			return Input.KEY_UNLABELED;
		}
		return defaultKeys.get(binding);
	}

	/**
	 * Returns a {@link KeyBinding} thats bound key is given key for given mode.
	 * 
	 * @param key
	 *            key to look for.
	 * @param mode
	 *            the interact mode the key is pressed in.
	 * @return binding assigned to given key.
	 * @throws KeyNotBoundException
	 *             if key is not assigned.
	 */
	public KeyBinding getBindingOf(int key, InteractMode mode)
			throws KeyNotBoundException {
		if (isBound(key, InteractMode.ANY_MODE)) {
			return keys.get(InteractMode.ANY_MODE).get(key);
		}
		if (isBound(key, mode)) {
			return keys.get(mode).get(key);
		} else {
			throw new KeyNotBoundException(key);
		}
	}

	/**
	 * Checks whether given key is bound in given interact mode.
	 * 
	 * @param key
	 *            key to check for.
	 * @param mode
	 *            interactmode the key should be bound in.
	 * @return true if key is bound, false otherwise.
	 */
	private boolean isBound(int key, InteractMode mode) {
		return (keys.get(mode).get(key) != null);
	}

	/**
	 * Checks whether given key is bound at all.
	 * 
	 * @param key
	 *            the key to check.
	 * @return true if given key is bound in any mode.
	 */
	public boolean isBound(int key) {
		for (InteractMode mode : InteractMode.values()) {
			if (isBound(key, mode))
				return true;
		}
		return false;
	}

}
