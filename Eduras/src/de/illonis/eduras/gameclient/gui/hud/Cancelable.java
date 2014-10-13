package de.illonis.eduras.gameclient.gui.hud;

/**
 * Classes which implement this interface can react on the KeyBinding.CANCLE
 * key.
 * 
 * @author Florian Mai
 *
 */
public interface Cancelable {

	/**
	 * Called to cancel the current action.
	 * 
	 * @return true if something was canceled, false otherwise.
	 */
	boolean cancel();
}
