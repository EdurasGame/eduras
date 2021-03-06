package de.illonis.eduras.interfaces;

import java.util.ArrayList;
import java.util.LinkedList;

import de.illonis.eduras.Team;
import de.illonis.eduras.events.AoEDamageEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.RoundEndEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetMapEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.units.Unit;

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
	 * Called when a resource cannot be found.
	 * 
	 * @param type
	 * @param resource
	 */
	void onResourceRequired(GameEventNumber type, String resource);

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
	 * Called when the visibility of an object changed.
	 * 
	 * @param event
	 *            the event.
	 */
	void onVisibilityChanged(SetVisibilityEvent event);

	/**
	 * Called when the game mode has changed.
	 * 
	 * @param newGameMode
	 *            new game mode.
	 */
	void onGameModeChanged(GameMode newGameMode);

	/**
	 * Called when health of a unit has changed.
	 * 
	 * @param unit
	 * @param oldValue
	 * @param newValue
	 * 
	 */
	void onHealthChanged(Unit unit, int oldValue, int newValue);

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
	 * Called when a unit respawns.
	 * 
	 * @param event
	 *            the event holding information.
	 */
	void onRespawn(RespawnEvent event);

	/**
	 * Called when a team conquers a base.
	 * 
	 * @param base
	 * @param conqueringTeam
	 */
	void onBaseConquered(Base base, Team conqueringTeam);

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

	/**
	 * Called when the amount of a team resource changed.
	 * 
	 * @param setTeamResourceEvent
	 *            the event data.
	 */
	void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent);

	/**
	 * Called when all initial information has been exchanged and the client is
	 * ready to go.
	 */
	void onGameReady();

	/**
	 * Called when a new player joins.
	 * 
	 * @param ownerId
	 *            the owner id of joined player.
	 */
	void onPlayerJoined(int ownerId);

	/**
	 * Called when a player leaves.
	 * 
	 * @param ownerId
	 *            the owner id of left player.
	 */
	void onPlayerLeft(int ownerId);

	/**
	 * Called when usage of an item failed.
	 * 
	 * @param itemFailedEvent
	 *            the event data.
	 */
	void onItemUseFailed(ItemUseFailedEvent itemFailedEvent);

	/**
	 * Called when the map changed.
	 * 
	 * @param setMapEvent
	 */
	void onMapChanged(SetMapEvent setMapEvent);

	/**
	 * Called when a new round starts.
	 */
	void onStartRound();

	/**
	 * Called when the teams are set.
	 * 
	 * @param teamList
	 *            list of all teams
	 */
	void onTeamsSet(LinkedList<Team> teamList);

	/**
	 * Called when AoE damage was done somewhere.
	 * 
	 * @param event
	 */
	void onAoEDamage(AoEDamageEvent event);

	/**
	 * Called when team of a player changed.
	 * 
	 * @param ownerId
	 *            owner of player.
	 */
	void onPlayerTeamChanged(int ownerId);

	/**
	 * Called when a round ended.
	 * 
	 * @param event
	 */
	void onRoundEnd(RoundEndEvent event);

	/**
	 * Called when a player used blink successfully.
	 * 
	 * @param owner
	 *            owner-id of blinked player.
	 */
	void onPlayerBlinked(int owner);
}
