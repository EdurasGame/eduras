package de.illonis.eduras.gameclient;

import java.util.ArrayList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.interfaces.GameEventListener;

public class GameEventReactor implements GameEventListener {

	private GameClient client;

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

}
