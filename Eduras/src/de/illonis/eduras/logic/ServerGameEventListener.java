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
		// TODO Auto-generated method stub

	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeath(DeathEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameReady() {
		// TODO Auto-generated method stub

	}

}
