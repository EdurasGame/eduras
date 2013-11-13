package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;
import java.util.LinkedList;

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
import de.illonis.eduras.gameclient.gui.hud.RenderedGuiObject;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * The eventlistener that listens for new events and passes them to gui
 * elements.
 * 
 * @author illonis
 * 
 */
public class HudNotifier implements GameEventListener {

	private LinkedList<RenderedGuiObject> uiObjects;

	/**
	 * Creates a new {@link HudNotifier}.
	 * 
	 */
	public HudNotifier() {
		this.uiObjects = new LinkedList<RenderedGuiObject>();
	}

	/**
	 * Sets the hud objects that should be notified by events.
	 * 
	 * @param objects
	 *            a list of gui objects that should be notified.
	 */
	public void setUiObjects(LinkedList<RenderedGuiObject> objects) {
		System.out.println("Set ui objects");
		this.uiObjects = objects;
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

	@Override
	public void onDeath(DeathEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onDeath(event);
		}
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onCooldownStarted(event);
		}
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onCooldownFinished(event);
		}
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onInteractModeChanged(setModeEvent);
		}
	}

	@Override
	public void onGameReady() {
	}
}
