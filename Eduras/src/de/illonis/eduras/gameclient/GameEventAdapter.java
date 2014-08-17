package de.illonis.eduras.gameclient;

import java.util.ArrayList;

import de.illonis.eduras.Team;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.RespawnEvent;
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
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.units.Unit;

/**
 * A simple implementation of {@link GameEventListener} that does nothing.
 * 
 * @author illonis
 * 
 */
public class GameEventAdapter implements GameEventListener {
	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onHealthChanged(Unit unit, int oldValue, int newValue) {
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
	}

	@Override
	public void onDeath(DeathEvent event) {
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
	}

	@Override
	public void onGameReady() {
	}

	@Override
	public void onVisibilityChanged(SetVisibilityEvent event) {
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
	}

	@Override
	public void onRespawn(RespawnEvent event) {
	}

	@Override
	public void onPlayerJoined(int ownerId) {
	}

	@Override
	public void onPlayerLeft(int ownerId) {
	}

	@Override
	public void onItemUseFailed(ItemUseFailedEvent itemFailedEvent) {
	}

	@Override
	public void onMapChanged(SetMapEvent setMapEvent) {
	}

	@Override
	public void onBaseConquered(Base base, Team conqueringTeam) {
	}
}
