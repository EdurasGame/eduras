package de.illonis.eduras.gameclient;

import java.util.ArrayList;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gameclient.gui.UserInterface;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * Reacts on game events on client side and passes them to client logic and gui.
 * 
 * @author illonis
 * 
 */
public class GameEventReactor implements GameEventListener {

	private final UserInterface ui;

	/**
	 * Creates a new reactor.
	 * 
	 * @param ui
	 *            associated user interface.
	 */
	public GameEventReactor(UserInterface ui) {
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
		if (event.getOwner() == ui.getInfos().getOwnerID()
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
	public void onHealthChanged(int objectId, int newValue) {
		ui.onHealthChanged(objectId, newValue);
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
		ui.onGameModeChanged(newGameMode);
	}
}
