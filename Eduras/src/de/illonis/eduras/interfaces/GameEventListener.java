package de.illonis.eduras.interfaces;

import java.util.ArrayList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.events.GameEvent;

public interface GameEventListener {

	/**
	 * This method is called if a gameobject got a new position.
	 * 
	 * @param object
	 *            The object.
	 */
	public void onNewObjectPosition(GameObject player);

	/**
	 * This method is called if the logic was asked to supply any information
	 * available. The information is given as a list of events providing all
	 * needed details.
	 * 
	 * @param infos The list of events representing the information.
	 */
	public void onInformationRequested(ArrayList<GameEvent> infos);
}
