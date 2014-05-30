package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

import de.illonis.eduras.gameclient.EdurasGameInterface;
import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.gui.GameManager;
import de.illonis.eduras.networking.discover.ServerInfo;

/**
 * Provides some access to the game itself from controllers.
 * 
 * @author illonis
 * 
 */
public interface GameControllerBridge extends GameManager {

	void exit();

	void enterState(int state);

	void enterState(int id, Transition leave, Transition enter);

	void changeResolution(int width, int height) throws SlickException;

	String getUsername();

	void setUsername(String name);

	void setServer(ServerInfo current);

	ServerInfo getServer();

	void initClient();

	EdurasGameInterface getEduras();

	void setLoginData(LoginData data);

	LoginData getLoginData();

}
