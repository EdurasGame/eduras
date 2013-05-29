/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import de.illonis.eduras.ClientGameMode;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.logic.ClientEventTriggerer;
import de.illonis.eduras.logic.Logic;
import de.illonis.eduras.networking.Client;
import de.illonis.eduras.settings.Settings;

/**
 * This class provides a main entry point to the game logic and network for GUI
 * developers. This is a singleton class.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EdurasInitializer {

	NetworkManager networkManager;
	EventSender eventSender;
	InformationProvider informationProvider;
	Settings settings;
	static EdurasInitializer instance;

	private EdurasInitializer() {
		instance = this;
		GameInformation game = new GameInformation();

		// needed because all the game game mode action shall be performed on
		// the server.
		game.getGameSettings().changeGameMode(new ClientGameMode());

		GameLogicInterface logic = new Logic(game);

		game.setEventTriggerer(new ClientEventTriggerer());

		networkManager = new NetworkManager(logic);

		Client client = networkManager.getClient();

		settings = new Settings();

		eventSender = new EventSender(client, logic);

		informationProvider = new InformationProvider(logic, networkManager);

	}

	/**
	 * Returns the {@link NetworkManager}.
	 * 
	 * @return The NetworkManager
	 */
	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	/**
	 * Returns the unique {@link EdurasInitializer} instance.
	 * 
	 * @return The EdurasInitializer instance
	 */
	public static EdurasInitializer getInstance() {
		if (instance == null) {
			return new EdurasInitializer();
		}
		return instance;
	}

	/**
	 * Returns the {@link EventSender}.
	 * 
	 * @return The EventSender
	 */
	public EventSender getEventSender() {
		return eventSender;
	}

	/**
	 * Returns the {@link InformationProvider}
	 * 
	 * @return The InformationProvider
	 */
	public InformationProvider getInformationProvider() {
		return informationProvider;
	}

	/**
	 * Returns the current {@link Settings} of the current game.
	 * 
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}
}
