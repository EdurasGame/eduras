package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetMapEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.gameclient.gui.hud.RenderedGuiObject;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * The eventlistener that listens for new events and passes them to all
 * displayed gui elements as well as other gui components that register
 * manually.
 * 
 * @author illonis
 * 
 */
public class HudNotifier implements GameEventListener {

	private LinkedList<RenderedGuiObject> uiObjects;
	private final LinkedList<GameEventListener> otherObjects;

	/**
	 * Creates a new {@link HudNotifier}.
	 * 
	 */
	public HudNotifier() {
		this.uiObjects = new LinkedList<RenderedGuiObject>();
		this.otherObjects = new LinkedList<GameEventListener>();
	}

	/**
	 * Sets the hud objects that should be notified by events.
	 * 
	 * @param objects
	 *            a list of gui objects that should be notified.
	 */
	public void setUiObjects(LinkedList<RenderedGuiObject> objects) {
		this.uiObjects = objects;
	}

	/**
	 * Adds a listener to this listener.
	 * 
	 * @param listener
	 *            the new listener.
	 */
	public void addListener(GameEventListener listener) {
		otherObjects.add(listener);
	}

	/**
	 * Removes given listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeListener(GameEventListener listener) {
		otherObjects.remove(listener);
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onMaxHealthChanged(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onMaxHealthChanged(event);
		}
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		for (GameEventListener obj : uiObjects) {
			obj.onNewObjectPosition(object);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onNewObjectPosition(object);
		}
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> events,
			int targetOwner) {
		for (GameEventListener obj : uiObjects) {
			obj.onInformationRequested(events, targetOwner);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onInformationRequested(events, targetOwner);
		}
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onObjectCreation(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onObjectCreation(event);
		}
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onClientRename(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onClientRename(event);
		}
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		for (GameEventListener obj : uiObjects) {
			obj.onObjectStateChanged(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onObjectStateChanged(event);
		}
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
		for (GameEventListener obj : uiObjects) {
			obj.onGameModeChanged(newGameMode);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onGameModeChanged(newGameMode);
		}
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onHealthChanged(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onHealthChanged(event);
		}
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onOwnerChanged(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onOwnerChanged(event);
		}
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onItemSlotChanged(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onItemSlotChanged(event);
		}
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onObjectRemove(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onObjectRemove(event);
		}
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onMatchEnd(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onMatchEnd(event);
		}
	}

	@Override
	public void onDeath(DeathEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onDeath(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onDeath(event);
		}
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onCooldownStarted(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onCooldownStarted(event);
		}
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onCooldownFinished(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onCooldownFinished(event);
		}
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		for (int i = 0; i < uiObjects.size(); i++) {
			GameEventListener obj = uiObjects.get(i);
			obj.onInteractModeChanged(setModeEvent);
		}
		for (int i = 0; i < uiObjects.size(); i++) {
			GameEventListener obj = uiObjects.get(i);
			obj.onInteractModeChanged(setModeEvent);
		}
	}

	@Override
	public void onGameReady() {
		for (GameEventListener obj : uiObjects) {
			obj.onGameReady();
		}
		for (GameEventListener obj : otherObjects) {
			obj.onGameReady();
		}
	}

	@Override
	public void onVisibilityChanged(SetVisibilityEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onVisibilityChanged(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onVisibilityChanged(event);
		}
	}

	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
		for (GameEventListener obj : uiObjects) {
			obj.onTeamResourceChanged(setTeamResourceEvent);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onTeamResourceChanged(setTeamResourceEvent);
		}
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		for (GameEventListener obj : uiObjects) {
			obj.onRespawn(event);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onRespawn(event);
		}
	}

	@Override
	public void onPlayerJoined(int ownerId) {
		for (GameEventListener obj : uiObjects) {
			obj.onPlayerJoined(ownerId);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onPlayerJoined(ownerId);
		}
	}

	@Override
	public void onPlayerLeft(int ownerId) {
		for (GameEventListener obj : uiObjects) {
			obj.onPlayerLeft(ownerId);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onPlayerLeft(ownerId);
		}
	}

	@Override
	public void onItemUseFailed(ItemUseFailedEvent itemFailedEvent) {
		for (GameEventListener obj : uiObjects) {
			obj.onItemUseFailed(itemFailedEvent);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onItemUseFailed(itemFailedEvent);
		}
	}

	@Override
	public void onMapChanged(SetMapEvent setMapEvent) {
		for (GameEventListener obj : uiObjects) {
			obj.onMapChanged(setMapEvent);
		}
		for (GameEventListener obj : otherObjects) {
			obj.onMapChanged(setMapEvent);
		}
	}
}
