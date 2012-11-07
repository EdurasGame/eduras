package de.illonis.eduras.settings;

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
	public enum KeyBinding {
		MOVE_LEFT, MOVE_UP, MOVE_DOWN, MOVE_RIGHT, SHOOT;
	}

	@Override
	public void loadDefaults() {

		keys.put(KeyEvent.VK_A, KeyBinding.MOVE_LEFT);
		keys.put(KeyEvent.VK_W, KeyBinding.MOVE_UP);
		keys.put(KeyEvent.VK_S, KeyBinding.MOVE_DOWN);
		keys.put(KeyEvent.VK_D, KeyBinding.MOVE_RIGHT);
		keys.put(KeyEvent.VK_SPACE, KeyBinding.SHOOT);
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
