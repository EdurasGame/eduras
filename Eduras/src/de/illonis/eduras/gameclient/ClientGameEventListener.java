package de.illonis.eduras.gameclient;

import java.util.ArrayList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

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

	/**
	 * Creates a new reactor.
	 * 
	 * @param client
	 *            associated client
	 * @param ui
	 *            associated user interface event listener.
	 */
	public ClientGameEventListener(GameClient client, HudNotifier ui) {
		this.client = client;
		this.ui = ui;
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		ui.onNewObjectPosition(object);
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		ui.onObjectCreation(event);
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		ui.onClientRename(event);
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		ui.onObjectStateChanged(event);
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		ui.onHealthChanged(event);
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		ui.onOwnerChanged(event);
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		ui.onItemSlotChanged(event);
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		ui.onObjectRemove(event);
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		ui.onMatchEnd(event);
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
		L.info("GameMode changed to " + newGameMode.getName());
		ui.onGameModeChanged(newGameMode);
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		ui.onMaxHealthChanged(event);
	}

	@Override
	public void onDeath(DeathEvent event) {
		ui.onDeath(event);
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
		ui.onCooldownStarted(event);
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
		ui.onCooldownFinished(event);
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		ui.onInteractModeChanged(setModeEvent);
		client.getLogic().resetCamera();
	}

	@Override
	public void onGameReady() {
		System.out.println("game ready");
		// Do not notify hud as it is not yet initialized. It will be notified
		// later on.
	}

	@Override
	public void onVisibilityChanged(SetVisibilityEvent event) {
		ui.onVisibilityChanged(event);
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		ui.onTeamResourceChanged(setTeamResourceEvent);
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		ui.onRespawn(event);
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		ui.onPlayerJoined(ownerId);
	}

	@Override
	public void onPlayerLeft(int ownerId) {
		ui.onPlayerLeft(ownerId);
	}
}
