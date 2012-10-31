package de.illonis.eduras.interfaces;

import de.illonis.eduras.GameObject;

public interface GameEventListener {
	
	/**
	 * This method is called if a gameobject got a new position.
	 * @param object The object.
	 */
	public void onNewObjectPosition(GameObject player);
}
