package de.illonis.eduras.gameclient.gui.game;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * This class handles user's key input and triggers the correspondend
 * {@link GuiKeyHandler} for current interactmode.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler {

	private final static Logger L = EduLog.getLoggerFor(InputKeyHandler.class
			.getName());

	private final InformationProvider infoPro;
	private static final long KEY_INTERVAL = 20;

	private final HashMap<InteractMode, GuiKeyHandler> keyHandlers;

	/**
	 * Used to support linux os. If a key is hold down on a linux system, it
	 * sends repeatedly pressed- and released-events. So we need a timeout.
	 */
	private final HashMap<Integer, Boolean> pressedButtons;
	private long lastTimePressed;

	private final Settings settings;
	private final GamePanelLogic client;
	private int currentKey = Input.KEY_UNLABELED;

	/**
	 * Creates a new input key handler.
	 * 
	 * @param client
	 *            associated client.
	 * @param reactor
	 *            the reactor that passes actions to server.
	 */
	public InputKeyHandler(GamePanelLogic client, GamePanelReactor reactor) {
		keyHandlers = new HashMap<InteractMode, GuiKeyHandler>();
		infoPro = EdurasInitializer.getInstance().getInformationProvider();
		this.settings = EdurasInitializer.getInstance().getSettings();
		pressedButtons = new HashMap<Integer, Boolean>();
		this.client = client;
		lastTimePressed = System.currentTimeMillis();
		keyHandlers.put(InteractMode.MODE_EGO, new EgoModeKeyHandler(client,
				reactor));
		keyHandlers.put(InteractMode.MODE_STRATEGY, new BuildModeKeyHandler(
				client, reactor));
	}

	/**
	 * Checks if given key was pressed recently. If given key is not assigned,
	 * false will be returned.
	 * 
	 * @param key
	 *            key to check.
	 * @return true if key was pressed recently, false otherwise.
	 */
	private boolean justPressed(int key) {
		if (pressedButtons.containsKey(key))
			return pressedButtons.get(key);
		else
			return false;
	}

	/**
	 * Releases all pressed keys and handles keyRelease events.
	 */
	public void releaseAllKeys() {
		for (java.util.Map.Entry<Integer, Boolean> button : pressedButtons
				.entrySet()) {
			if (button.getValue()) {
				// release button
				keyReleased(button.getKey(), ' ');
			}
		}
	}

	/**
	 * Indicates that a key was pressed.
	 * 
	 * @param key
	 *            the key pressed (see {@link Input}).
	 * @param c
	 *            the key char.
	 */
	public void keyPressed(int key, char c) {
		boolean consumed = false;
		if (key != settings.getKeyBindings().getKey(KeyBinding.CHAT)
				&& key != settings.getKeyBindings().getKey(
						KeyBinding.EXIT_CLIENT))
			consumed = client.onKeyType(key, c);

		if (consumed) {
			currentKey = key;
			return;
		}

		int keyCode = key;
		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			return;
		}

		// if already pressed, do not send a new event
		if (justPressed(keyCode))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

		pressedButtons.put(keyCode, true);
		if (infoPro.getGameMode().supportsKeyBinding(binding)) {
			PlayerMainFigure player;
			try {
				player = infoPro.getPlayer();
			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE, "Something terribly bad happened.", e1);
				return;
			}
			GuiKeyHandler handler = keyHandlers.get(player.getCurrentMode());
			if (handler != null) {
				try {
					handler.keyPressed(binding);
				} catch (ActionFailedException e) {
					client.onActionFailed(e);
				}
			}
		}

		lastTimePressed = System.currentTimeMillis();
		L.fine("Bound key pressed: " + keyCode);
	}

	/**
	 * Indicates a key release.
	 * 
	 * @param key
	 *            the key code. See fields of {@link Input}.
	 * @param c
	 *            the key char.
	 */
	public void keyReleased(int key, char c) {

		if (key == currentKey) {
			currentKey = Input.KEY_UNLABELED;
			return;
		}
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(key))
			return;

		if (lastTimePressed < KEY_INTERVAL)
			return;

		// release button
		pressedButtons.put(key, false);

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(key);
		} catch (KeyNotBoundException ex) {
			L.log(Level.SEVERE, "Key is bound but receiving binding failed: "
					+ key, ex);
			return;
		}
		if (infoPro.getGameMode().supportsKeyBinding(binding)) {

			PlayerMainFigure player;
			try {
				player = infoPro.getPlayer();
			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE, "Something terribly bad happened.", e1);
				return;
			}
			GuiKeyHandler handler = keyHandlers.get(player.getCurrentMode());
			if (handler != null) {
				handler.keyReleased(binding);
			}
		}

	}

}