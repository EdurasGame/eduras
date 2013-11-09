package de.illonis.eduras.gameclient.gui;

import de.illonis.eduras.gameclient.LoginData;

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
