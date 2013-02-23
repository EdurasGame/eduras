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
 * developers.
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

		eventSender = new EventSender(client);

		informationProvider = new InformationProvider(logic, networkManager);

	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public static EdurasInitializer getInstance() {
		if (instance == null) {
			return new EdurasInitializer();
		}
		return instance;
	}

	public EventSender getEventSender() {
		return eventSender;
	}

	public InformationProvider getInformationProvider() {
		return informationProvider;
	}

	public Settings getSettings() {
		return settings;
	}

	public void shutdown() {
		networkManager.disconnect();
	}

}
