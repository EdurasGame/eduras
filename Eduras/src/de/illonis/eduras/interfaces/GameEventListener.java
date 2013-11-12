package de.illonis.eduras.interfaces;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;

/**
 * Reacts on game events and performs action.
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
	 * Called when a gameobject has been created.
	 * 
	 * @param event
	 *            the corresponding event.
	 */
	void onObjectCreation(ObjectFactoryEvent event);

	/**
	 * Fired when a client has a new name.
	 * 
	 * @param event
	 *            event holding data.
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
	 * @param event
	 *            the event holding information.
	 */
	void onHealthChanged(SetIntegerGameObjectAttributeEvent event);

	/**
	 * Called when maximum health of an object has changed.
	 * 
	 * @param event
	 *            the event holding information.
	 */
	void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event);

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
	 *            the event holding information.
	 */
	void onObjectRemove(ObjectFactoryEvent event);

	/**
	 * Called when a match ended.
	 * 
	 * @param event
	 *            the event holding information.
	 */
	void onMatchEnd(MatchEndEvent event);

	/**
	 * Called when a unit is killed.
	 * 
	 * @param event
	 *            the event holding information.
	 * 
	 * @author illonis
	 */
	void onDeath(DeathEvent event);

	/**
	 * Called when cooldown of an item has finished.
	 * 
	 * @param event
	 *            finish event.
	 * 
	 * @author illonis
	 */
	void onCooldownStarted(ItemEvent event);

	/**
	 * Called when cooldown of an item has started.
	 * 
	 * @param event
	 *            start event.
	 * 
	 * @author illonis
	 */
	void onCooldownFinished(ItemEvent event);

	/**
	 * Called when interact mode of a player changed.
	 * 
	 * @param setModeEvent
	 *            mode change event
	 */
	void onInteractModeChanged(SetInteractModeEvent setModeEvent);

	void onGameReady();
}
