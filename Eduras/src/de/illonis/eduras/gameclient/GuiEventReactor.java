package de.illonis.eduras.gameclient;

import java.util.ArrayList;

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
import de.illonis.eduras.gameclient.gui.EventListenerGui;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * Reacts on game events on client side and passes them to gui.
 * 
 * @author illonis
 * 
 */
public class GuiEventReactor implements GameEventListener {

	private final EventListenerGui ui;
	private final NetworkEventReactor networkEventReactor;

	/**
	 * Creates a new reactor.
	 * 
	 * @param ui
	 *            associated user interface event listener.
	 */
	public GuiEventReactor(EventListenerGui ui,
			NetworkEventReactor networkEventReactor) {
		this.networkEventReactor = networkEventReactor;
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
		if (setModeEvent.getOwner() == ui.getInfos().getOwnerID()) {
			ui.onInteractModeChanged(setModeEvent);
		}

	}

	@Override
	public void onGameReady() {
		networkEventReactor.onGameReady();
	}
}
