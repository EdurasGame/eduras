package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * The base structure for a keyhandler.
 * 
 * @author illonis
 * 
 */
public abstract class GuiKeyHandler {

	protected final Settings settings;
	protected final GamePanelReactor reactor;
	protected final GamePanelLogic client;

	GuiKeyHandler(GamePanelLogic client, GamePanelReactor reactor) {
		this.settings = EdurasInitializer.getInstance().getSettings();
		this.reactor = reactor;
		this.client = client;
	}

	/**
	 * Indicates that a key was pressed.
	 * 
	 * @param key
	 *            the pressed binding.
	 */
	abstract void keyPressed(KeyBinding key) throws ActionFailedException;

	/**
	 * 
	 * @param key
	 */
	abstract void keyReleased(KeyBinding key);

}
