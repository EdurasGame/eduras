package de.illonis.eduras.gameclient.gui.game;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.KeyNotBoundException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ChatCache;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.networking.ClientRole;
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

	private final HashMap<InteractMode, GuiKeyHandler> keyHandlers;
	private final GuiKeyHandler spectatorKeyHandler;

	private final HashMap<Integer, Boolean> pressedButtons;
	private final ChatCache cache;

	private final Settings settings;
	private final UserInputListener client;

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
		keyHandlers.put(InteractMode.MODE_EGO, new EgoModeKeyHandler(client,
				reactor));
		keyHandlers.put(InteractMode.MODE_STRATEGY, new BuildModeKeyHandler(
				client, reactor));
		keyHandlers.put(InteractMode.MODE_DEAD, new DeadModeKeyHandler(client,
				reactor));
		spectatorKeyHandler = new SpectatorKeyHandler(client, reactor);
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
		boolean consumed = false;
		if (key != settings.getKeyBindings().getKey(KeyBinding.CHAT)
				&& key != settings.getKeyBindings().getKey(KeyBinding.CANCEL))
			consumed = onChatType(key, c);

		if (consumed) {
			return;
		}
		if (infoPro.getClientData().getRole() == ClientRole.SPECTATOR) {
			try {
				KeyBinding binding = settings.getKeyBindings()
						.getSpectatorBinding(key);
				spectatorKeyHandler.keyPressed(binding);
			} catch (KeyNotBoundException e) {
			}
			return;
		}
		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Could not find player while pressing key.", e1);
			return;
		}
		InteractMode currentMode = player.getCurrentMode();

		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(keyCode,
					currentMode);
		} catch (KeyNotBoundException ex) {
			return;
		}

		if (infoPro.getGameMode().supportsKeyBinding(binding)) {
			GuiKeyHandler handler = keyHandlers.get(player.getCurrentMode());
			if (handler != null) {
				if (handler.isChatEnabled()) {
					if (binding == KeyBinding.CHAT)
						client.onChatEnter();
					else if (binding == KeyBinding.CANCEL
							&& !client.abortChat()) {
						client.cancel();
					} else {
						handler.keyPressed(binding);
					}
				} else {
					handler.keyPressed(binding);
				}
			}
		}
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

		if (infoPro.getClientData().getRole() == ClientRole.SPECTATOR) {
			try {
				KeyBinding binding = settings.getKeyBindings()
						.getSpectatorBinding(key);
				spectatorKeyHandler.keyReleased(binding);
			} catch (KeyNotBoundException e) {
			}
			return;
		}
		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Could not find player while releasing key.",
					e1);
			return;
		}
		KeyBinding binding;
		try {
			binding = settings.getKeyBindings().getBindingOf(key,
					player.getCurrentMode());
		} catch (KeyNotBoundException ex) {
			return;
		}
		if (infoPro.getGameMode().supportsKeyBinding(binding)) {
			GuiKeyHandler handler = keyHandlers.get(player.getCurrentMode());
			if (handler != null) {
				handler.keyReleased(binding);
			}
		}

	}

}