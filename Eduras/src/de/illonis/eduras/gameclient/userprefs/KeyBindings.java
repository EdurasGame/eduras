package de.illonis.eduras.gameclient.userprefs;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.interfaces.ResettableSetting;

/**
 * Maps keyboard keys to user-specific keybindings.
 * 
 * @author illonis
 * 
 */
public final class KeyBindings implements ResettableSetting {

	private HashMap<Integer, KeyBinding> keys;

	/**
	 * Initialize keybindings.
	 */
	public KeyBindings() {
		keys = new HashMap<Integer, KeyBinding>();
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

	@Override
	public void loadDefaults() {
		setKeyBinding(KeyBinding.MOVE_LEFT, KeyEvent.VK_A);
		setKeyBinding(KeyBinding.MOVE_RIGHT, KeyEvent.VK_D);
		setKeyBinding(KeyBinding.MOVE_DOWN, KeyEvent.VK_S);
		setKeyBinding(KeyBinding.MOVE_UP, KeyEvent.VK_W);
		setKeyBinding(KeyBinding.ITEM_1, KeyEvent.VK_1);
		setKeyBinding(KeyBinding.ITEM_2, KeyEvent.VK_2);
		setKeyBinding(KeyBinding.ITEM_3, KeyEvent.VK_3);
		setKeyBinding(KeyBinding.ITEM_4, KeyEvent.VK_4);
		setKeyBinding(KeyBinding.ITEM_5, KeyEvent.VK_5);
		setKeyBinding(KeyBinding.ITEM_6, KeyEvent.VK_6);
		setKeyBinding(KeyBinding.CHAT, KeyEvent.VK_ENTER);
		setKeyBinding(KeyBinding.SWITCH_MODE, KeyEvent.VK_M);
		setKeyBinding(KeyBinding.EXIT_CLIENT, KeyEvent.VK_ESCAPE);
		setKeyBinding(KeyBinding.SHOW_STATS, KeyEvent.VK_TAB);
	}

	/**
	 * Sets the key binding for a specified binding.<br>
	 * Use {@link KeyEvent#VK_UNDEFINED} to set key to <i>None</i>. This
	 * will overwrite any existing key for this binding as each binding
	 * can only have one key assigned.
	 * 
	 * @param binding
	 *            binding to set.
	 * @param key
	 *            new key, {@link KeyEvent#VK_UNDEFINED} for none.
	 */
	private void setKeyBinding(KeyBinding binding, int key) {
		keys.put(key, binding);
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
