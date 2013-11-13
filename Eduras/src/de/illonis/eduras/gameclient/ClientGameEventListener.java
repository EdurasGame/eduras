package de.illonis.eduras.gameclient;

import java.util.ArrayList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
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
	private boolean wasReady = false;

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
	public void onNewObjectPosition(GameObject player) {

	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		// if our player was created, player data is there.
		if (event.getOwner() == client.getOwnerID()
				&& event.getObjectType() == ObjectType.PLAYER)
			ui.onPlayerReceived();
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
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		ui.onMatchEnd(event);
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
		System.out.println("gamemode changed to: " + newGameMode);
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
	}

	@Override
	public void onGameReady() {
		if (wasReady) {
			L.severe("Received onGameReady a second time!");
			return;
		}
		wasReady = true;
		client.getFrame().startAndShowGame();
		// Do not notify hud as it is not yet initialized. It will be notified
		// later on.
	}
}
