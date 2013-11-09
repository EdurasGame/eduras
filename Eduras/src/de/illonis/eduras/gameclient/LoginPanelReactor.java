package de.illonis.eduras.gameclient;


/**
 * Reacts on login panel's actions.
 * 
 * @author illonis
 * 
 */
public interface LoginPanelReactor {

	/**
	 * Starts the login procedure.
	 * 
	 * @param data
	 *            the authentication information.
	 */
	void login(LoginData data);
}
