package de.illonis.eduras.logic;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.ServerInterface;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;
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
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * Defines how the server reacts on certain events in the logic that are the
 * same on client and server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerGameEventListener implements GameEventListener {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private ServerInterface server;

	/**
	 * Create a new ServerGameEventListener.
	 * 
	 * @param server
	 */
	public ServerGameEventListener(ServerInterface server) {
		this.server = server;
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		try {
			server.sendEventToAll(event);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.log(Level.SEVERE, "Error appeared while trying to send event.", e);
		}
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
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
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
	}

	@Override
	public void onGameReady() {
	}

	@Override
	public void onVisibilityChanged(SetVisibilityEvent event) {
	}

}
