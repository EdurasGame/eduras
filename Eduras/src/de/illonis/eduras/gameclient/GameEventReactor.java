package de.illonis.eduras.gameclient;

import java.util.ArrayList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.interfaces.GameEventListener;

public class GameEventReactor implements GameEventListener {

	private final GameClient client;

	public GameEventReactor(GameClient gameClient) {
		this.client = gameClient;
	}

	@Override
	public void onNewObjectPosition(GameObject player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		// if our player was created, player data is there.
		if (event.getOwner() == client.getInformationProvider().getOwnerID()
				&& event.getObjectType() == ObjectType.PLAYER)
			client.onPlayerReceived();
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHealthChanged(int objectId, int newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {

	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		client.getFrame().getItemDisplay().onItemChanged(event.getItemSlot());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.interfaces.GameEventListener#onObjectRemove(de.illonis
	 * .eduras.events.ObjectFactoryEvent)
	 */
	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {

		// TODO: Implement!

	}

}
