/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Logic;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.networking.Client;

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
	static EdurasInitializer instance;
	
	private EdurasInitializer() {
		
		GameInformation game = new GameInformation();
		
		GameLogicInterface logic = new Logic(game);
		
		networkManager = new NetworkManager(logic);
		
		Client client = networkManager.getClient();
		
		eventSender = new EventSender(client);
		
		informationProvider = new InformationProvider(logic,client.getOwnerId());
		
	}
	
	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public static EdurasInitializer getInstance() {
		if(instance == null) {
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
	
}
