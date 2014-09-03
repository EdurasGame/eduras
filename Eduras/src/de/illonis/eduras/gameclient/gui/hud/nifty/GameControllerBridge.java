package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

import de.illonis.eduras.gameclient.EdurasGameInterface;
import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.gui.GameManager;
import de.illonis.eduras.networking.discover.ServerInfo;

/**
 * Provides some access to the game itself from gui controllers.
 * 
 * @author illonis
 * 
 */
public interface GameControllerBridge extends GameManager {

	/**
	 * Exits the game.s
	 */
	void exit();

	/**
	 * Switches the gui state. Available states can be seen in
	 * {@link EdurasSlickClient.Game#initStatesList(org.newdawn.slick.GameContainer)}
	 * .
	 * 
	 * @param state
	 *            the new state.
	 */
	void enterState(int state);

	/**
	 * Switches the gui state with a custom transition.
	 * 
	 * @param state
	 *            the new state.
	 * @param leave
	 *            the leave transition of the former state.
	 * @param enter
	 *            the enter transition for the new state.
	 * @see #enterState(int)
	 */
	void enterState(int state, Transition leave, Transition enter);

	/**
	 * Changes the resolution of the game display.
	 * 
	 * @param width
	 *            new width.
	 * @param height
	 *            new height.
	 * @throws SlickException
	 *             if there is a display error.
	 */
	void changeResolution(int width, int height) throws SlickException;

	/**
	 * @return the username of the logged in user.
	 */
	String getUsername();

	/**
	 * Sets the current username.
	 * 
	 * @param name
	 *            the new name.
	 */
	void setUsername(String name);

	/**
	 * Sets the server to connect to.
	 * 
	 * @param current
	 *            the server.
	 */
	void setServer(ServerInfo current);

	/**
	 * @return the target server.
	 */
	ServerInfo getServer();

	/**
	 * Sets up the eduras game client and passes mouselisteners, etc.
	 */
	void initClient();

	/**
	 * @return the underlying game interface.
	 */
	EdurasGameInterface getEduras();

	/**
	 * Sets the login credentials for later use.
	 * 
	 * @param data
	 *            the login data.
	 */
	void setLoginData(LoginData data);

	/**
	 * @return the login data.
	 */
	LoginData getLoginData();

	/**
	 * Sets the volume for sound effects.
	 * 
	 * @param volume
	 *            new value.
	 */
	void setSoundVolume(float volume);

	/**
	 * @return the volume of sound effects.
	 */
	float getSoundVolume();

	/**
	 * Sets the volume for music.
	 * 
	 * @param volume
	 *            new value.
	 */
	void setMusicVolume(float volume);

	/**
	 * @return the volume of music.
	 */
	float getMusicVolume();

}
