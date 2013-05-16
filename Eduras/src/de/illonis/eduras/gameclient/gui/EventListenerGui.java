package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * The eventlistener that listens for new events and passes them to gui
 * elements.
 * 
 * @author illonis
 * 
 */
public class EventListenerGui implements GameEventListener {

	private ArrayList<RenderedGuiObject> uiObjects;
	private InformationProvider infos;

	/**
	 * Creates a new {@link EventListenerGui}.
	 * 
	 * @param infos
	 *            game information to use.
	 */
	public EventListenerGui(InformationProvider infos,
			ArrayList<RenderedGuiObject> uiObjects) {
		this.infos = infos;
		this.uiObjects = uiObjects;
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onMaxHealthChanged(event);
		}
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onNewObjectPosition(object);
		}
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> events,
			int targetOwner) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onInformationRequested(events, targetOwner);
		}
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onObjectCreation(event);
		}
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onClientRename(event);
		}
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onObjectStateChanged(event);
		}
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onGameModeChanged(newGameMode);
		}
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onHealthChanged(event);
		}
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onOwnerChanged(event);
		}
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onItemSlotChanged(event);
		}
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onObjectRemove(event);
		}
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onMatchEnd(event);
		}
	}

	/**
	 * Notifies gui objects that player object has been received.
	 */
	public void onPlayerReceived() {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onPlayerInformationReceived();
		}
	}

	/***
	 * Returns game information.
	 * 
	 * @return game information.
	 * 
	 * @author illonis
	 */
	public InformationProvider getInfos() {
		return infos;
	}

}