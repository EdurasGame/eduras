package de.illonis.eduras.gameclient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
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
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.animation.ClientEffectHandler;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.units.Unit;

/**
 * Reacts on game events on client side and passes them to gui.
 * 
 * @author illonis
 * 
 */
public class ClientGameEventListener implements GameEventListener {

	private final static Logger L = EduLog
			.getLoggerFor(ClientGameEventListener.class.getName());

	private final HudNotifier ui;
	private final GameClient client;
	private final ClientEffectHandler effects;

	/**
	 * Creates a new reactor.
	 * 
	 * @param client
	 *            associated client
	 * @param ui
	 *            associated user interface event listener.
	 * @param effects
	 *            associated effect handler.
	 */
	public ClientGameEventListener(GameClient client, HudNotifier ui,
			ClientEffectHandler effects) {
		this.client = client;
		this.ui = ui;
		this.effects = effects;
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		ui.onNewObjectPosition(object);
		effects.onNewObjectPosition(object);
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		ui.onObjectCreation(event);
		effects.onObjectCreation(event);
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		ui.onClientRename(event);
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		ui.onObjectStateChanged(event);
		effects.onObjectStateChanged(event);
	}

	@Override
	public void onHealthChanged(Unit unit, int oldValue, int newValue) {
		ui.onHealthChanged(unit, oldValue, newValue);
		effects.onHealthChanged(unit, oldValue, newValue);
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		ui.onOwnerChanged(event);
		effects.onOwnerChanged(event);
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		ui.onItemSlotChanged(event);
		effects.onItemSlotChanged(event);
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		ui.onObjectRemove(event);
		effects.onObjectRemove(event);
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		ui.onMatchEnd(event);
		effects.onMatchEnd(event);
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
		L.info("GameMode changed to " + newGameMode.getName());
		ui.onGameModeChanged(newGameMode);
		effects.onGameModeChanged(newGameMode);
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		ui.onMaxHealthChanged(event);
		effects.onMaxHealthChanged(event);
	}

	@Override
	public void onDeath(DeathEvent event) {
		ui.onDeath(event);
		effects.onDeath(event);
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
		ui.onCooldownStarted(event);
		effects.onCooldownStarted(event);
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
		ui.onCooldownFinished(event);
		effects.onCooldownFinished(event);
		client.getLogic().onCooldownFinished(event);
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		ui.onInteractModeChanged(setModeEvent);
		effects.onInteractModeChanged(setModeEvent);
		client.getLogic().onInteractModeChanged(setModeEvent);
	}

	@Override
	public void onGameReady() {
		ui.onGameReady();
		effects.onGameReady();
		client.onGameReady();
	}

	@Override
	public void onVisibilityChanged(SetVisibilityEvent event) {
		ui.onVisibilityChanged(event);
		effects.onVisibilityChanged(event);
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		ui.onTeamResourceChanged(setTeamResourceEvent);
		effects.onTeamResourceChanged(setTeamResourceEvent);
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		ui.onRespawn(event);
		effects.onRespawn(event);
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		ui.onPlayerJoined(ownerId);
		effects.onPlayerJoined(ownerId);
	}

	@Override
	public void onPlayerLeft(int ownerId) {
		ui.onPlayerLeft(ownerId);
		effects.onPlayerLeft(ownerId);
	}

	@Override
	public void onItemUseFailed(ItemUseFailedEvent itemFailedEvent) {
		ui.onItemUseFailed(itemFailedEvent);
		effects.onItemUseFailed(itemFailedEvent);
	}

	@Override
	public void onMapChanged(SetMapEvent setMapEvent) {
		ui.onMapChanged(setMapEvent);
		effects.onMapChanged(setMapEvent);
	}

	@Override
	public void onBaseConquered(Base base, Team conqueringTeam) {
		ui.onBaseConquered(base, conqueringTeam);
		effects.onBaseConquered(base, conqueringTeam);
	}

	@Override
	public void onStartRound() {
		ui.onStartRound();
		effects.onStartRound();
	}

	@Override
	public void onResourceRequired(GameEventNumber type, String resource) {
		client.requestResource(type, resource);
	}

	@Override
	public void onTeamsSet(LinkedList<Team> teamList) {
		ui.onTeamsSet(teamList);
	}

	@Override
	public void onAoEDamage(AoEDamageEvent event) {
		effects.onAoEDamage(event);
	}

	@Override
	public void onPlayerTeamChanged(int ownerId) {
		ui.onPlayerTeamChanged(ownerId);
		effects.onPlayerTeamChanged(ownerId);
	}

	@Override
	public void onRoundEnd(RoundEndEvent event) {
		ui.onRoundEnd(event);
		effects.onRoundEnd(event);
	}
}
