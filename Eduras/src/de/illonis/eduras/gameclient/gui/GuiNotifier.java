package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * Listens for game events and notifies all gui elements.
 * 
 * @author illonis
 * 
 */
public class GuiNotifier implements GameEventListener {
	private ArrayList<RenderedGuiObject> uiObjects;

	public GuiNotifier(ArrayList<RenderedGuiObject> uiObjects) {
		this.uiObjects = uiObjects;
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onNewObjectPosition(object);
		}
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onInformationRequested(infos, targetOwner);
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
	public void onHealthChanged(int objectId, int newValue) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onHealthChanged(objectId, newValue);
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

	/**
	 * Notifies gui objects that game panel size has changed.
	 * 
	 * @param width
	 *            new width.
	 * @param height
	 *            new height.
	 */
	public void onGuiSizeChanged(int width, int height) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onGuiSizeChanged(width, height);
		}
	}
}
