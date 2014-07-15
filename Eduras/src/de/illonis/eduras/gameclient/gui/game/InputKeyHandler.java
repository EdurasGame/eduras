package de.illonis.eduras.gameclient.gui.game;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ChatCache;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.InteractMode;

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
	private static final long KEY_INTERVAL = 5;

	private final HashMap<InteractMode, GuiKeyHandler> keyHandlers;

	/**
	 * Used to support linux os. If a key is hold down on a linux system, it
	 * sends repeatedly pressed- and released-events. So we need a timeout.
	 */
	private final HashMap<Integer, Boolean> pressedButtons;
	private long lastTimePressed;
	private final ChatCache cache;

	private final Settings settings;
	private final UserInputListener client;
	private int currentKey = Input.KEY_UNLABELED;

	/**
	 * Creates a new input key handler.
	 * 
	 * @param client
	 *            associated client.
	 * @param reactor
	 *            the reactor that passes actions to server.
	 */
	public InputKeyHandler(UserInputListener client, GamePanelReactor reactor) {
		EdurasInitializer init = EdurasInitializer.getInstance();
		keyHandlers = new HashMap<InteractMode, GuiKeyHandler>();
		infoPro = init.getInformationProvider();
		this.settings = init.getSettings();
		pressedButtons = new HashMap<Integer, Boolean>();
		cache = init.getInformationProvider().getClientData().getChatCache();
		this.client = client;
		lastTimePressed = System.currentTimeMillis();
		keyHandlers.put(InteractMode.MODE_EGO, new EgoModeKeyHandler(client,
				reactor));
		keyHandlers.put(InteractMode.MODE_STRATEGY, new BuildModeKeyHandler(
				client, reactor));
		keyHandlers.put(InteractMode.MODE_DEAD, new DeadModeKeyHandler(client,
				reactor));
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
	 * Handles key input for chat.
	 * 
	 * @param key
	 *            the key number.
	 * @param c
	 *            the character.
	 * @return true if keyevent was consumed.
	 */
	public boolean onChatType(int key, char c) {
		if (cache.isWriting()) {
			cache.write(key, c);
			return true;
		}
		return false;
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
		int keyCode = key;
		// if already pressed, do not send a new event
		if (justPressed(keyCode))
			return;
		currentKey = key;
		lastTimePressed = System.currentTimeMillis();

		if (lastTimePressed < KEY_INTERVAL)
			return;
		pressedButtons.put(keyCode, true);
		boolean consumed = false;
		if (key != settings.getKeyBindings().getKey(KeyBinding.CHAT)
				&& key != settings.getKeyBindings().getKey(
						KeyBinding.EXIT_CLIENT))
			consumed = onChatType(key, c);
		
		if (consumed) {
			return;
		}

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode);
		} catch (KeyNotBoundException ex) {
			return;
		}

		pressedButtons.put(keyCode, true);
		if (infoPro.getGameMode().supportsKeyBinding(binding)) {
			Player player;
			try {
				player = infoPro.getPlayer();
			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE, "Something terribly bad happened.", e1);
				return;
			}
			GuiKeyHandler handler = keyHandlers.get(player.getCurrentMode());
			if (handler != null) {
				try {
					if (handler.isChatEnabled()) {
						if (binding == KeyBinding.CHAT)
							client.onChatEnter();
						else if (binding == KeyBinding.EXIT_CLIENT
								&& !client.abortChat()) {
							client.onGameQuit();
						} else {
							handler.keyPressed(binding);
						}
					} else {
						handler.keyPressed(binding);
					}
				} catch (ActionFailedException e) {
					L.log(Level.WARNING, "Action failed.", e);
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
		// release button
		pressedButtons.put(key, false);
		if (key == currentKey && lastTimePressed < KEY_INTERVAL) {
			currentKey = Input.KEY_UNLABELED;
			return;
		}
		// don't handle other keys
		if (!settings.getKeyBindings().isBound(key))
			return;

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(key);
		} catch (KeyNotBoundException ex) {
			L.log(Level.SEVERE, "Key is bound but receiving binding failed: "
					+ key, ex);
			return;
		}
		if (infoPro.getGameMode().supportsKeyBinding(binding)) {

			Player player;
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