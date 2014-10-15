package de.illonis.eduras.gameclient.userprefs;

import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.units.InteractMode;

/**
 * Maps keybindings to interact modes.
 * 
 * @author illonis
 * 
 */
public class InteractModeKeys {

	/**
	 * Returns the interact mode given binding is active in.
	 * 
	 * @param binding
	 *            the binding to check.
	 * @return the interact mode.
	 */
	public static InteractMode getModeOfBinding(KeyBinding binding) {
		switch (binding) {
		case BLINK:
		case MOVE_DOWN:
		case MOVE_LEFT:
		case MOVE_UP:
		case MOVE_RIGHT:
		case ITEM_SWORD:
		case ITEM_ASSAULT:
		case ITEM_MINE:
		case ITEM_ROCKET:
		case ITEM_SIMPLE:
		case ITEM_SNIPER:
		case ITEM_SPLASH:
			return InteractMode.MODE_EGO;
		case CHAT:
		case SWITCH_MODE:
		case SELECT_TEAM:
		case CANCEL:
		case SHOW_STATS:
			return InteractMode.ANY_MODE;
		case STRATEGY_1:
		case STRATEGY_2:
		case STRATEGY_3:
		case STRATEGY_4:
		case STRATEGY_5:
		case STRATEGY_6:
		case STRATEGY_7:
		case STRATEGY_8:
		case STRATEGY_9:
		case STRATEGY_10:
		case SCROLL_UP:
		case SCROLL_LEFT:
		case SCROLL_DOWN:
		case SCROLL_RIGHT:
			return InteractMode.MODE_STRATEGY;
		}
		return InteractMode.ANY_MODE;
	}
}
