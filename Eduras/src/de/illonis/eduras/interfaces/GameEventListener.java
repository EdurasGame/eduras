package de.illonis.eduras.interfaces;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;

/**
 * reacts on game events and performs action.
 * 
 * @author illonis
 * 
 */
public interface GameEventListener {

	/**
	 * This method is called if a gameobject got a new position.
	 * 
	 * @param object
	 *            The object.
	 */
	void onNewObjectPosition(GameObject object);

	/**
	 * This method is called if the logic was asked to supply any information
	 * available. The information is given as a list of events providing all
	 * needed details.
	 * 
	 * @param infos
	 *            The list of events representing the information.
	 * @param targetOwner
	 *            owner that requested information.
	 */
	void onInformationRequested(ArrayList<GameEvent> infos, int targetOwner);

	/**
	 * bla
	 * 
	 * @param event
	 */
	void onObjectCreation(ObjectFactoryEvent event);

	/**
	 * Fired when a client has a new name.
	 * 
	 * @param event
	 *            event.
	 */
	void onClientRename(ClientRenameEvent event);

	/**
	 * Called when the state of an object has changed.
	 * 
	 * @param event
	 *            The correlating event.
	 */
	void onObjectStateChanged(SetGameObjectAttributeEvent<?> event);

	/**
	 * Called when the game mode has changed.
	 * 
	 * @param newGameMode
	 *            new game mode.
	 */
	void onGameModeChanged(GameMode newGameMode);

	/**
	 * Called when health of an object has changed.
	 * 
	 * @param objectId
	 *            object id.
	 * @param newValue
	 *            new health value.
	 */
	void onHealthChanged(int objectId, int newValue);

	/**
	 * Called when owner of an object has changed.
	 * 
	 * @param event
	 *            event containing new owner and object.
	 */
	void onOwnerChanged(SetOwnerEvent event);

	/**
	 * Called when an item slot has changed.
	 * 
	 * @param event
	 *            corresponding information.
	 */
	void onItemSlotChanged(SetItemSlotEvent event);

	/**
	 * Called when an object is removed.
	 * 
	 * @param event
	 */
	void onObjectRemove(ObjectFactoryEvent event);

	/**
	 * Called when a match ended.
	 * 
	 * @param event
	 */
	void onMatchEnd(MatchEndEvent event);
}
