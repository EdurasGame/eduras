package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.gameclient.gui.guielements.GameStatBar;
import de.illonis.eduras.gameclient.gui.guielements.ItemDisplay;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Listens for game events and notifies all gui elements.
 * 
 * @author illonis
 * 
 */
public class UserInterface implements GameEventListener {
	private ArrayList<RenderedGuiObject> uiObjects;
	private InformationProvider infos;
	private GuiClickReactor reactor;
	private TooltipHandler tooltipHandler;

	UserInterface(InformationProvider infos, GuiClickReactor clickReactor,
			TooltipHandler tooltipHandler) {
		this.uiObjects = new ArrayList<RenderedGuiObject>();
		this.infos = infos;
		this.reactor = clickReactor;
		this.tooltipHandler = tooltipHandler;
		createElements();
	}

	private void createElements() {

		ItemDisplay itemDisplay = new ItemDisplay(this);
		uiObjects.add(itemDisplay);
		GameStatBar statBar = new GameStatBar(this);
		uiObjects.add(statBar);
	}

	/**
	 * Returns informationprovider.
	 * 
	 * @return informationprovider.
	 */
	public InformationProvider getInfo() {
		return infos;
	}

	ArrayList<RenderedGuiObject> getUiObjects() {
		return uiObjects;
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
	void onGuiSizeChanged(int width, int height) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onGuiSizeChanged(width, height);
		}
	}

	/**
	 * Removes given gui object from this userinterface.
	 * 
	 * @param renderedGuiObject
	 *            object to remove.
	 */
	public void removeGuiElement(RenderedGuiObject renderedGuiObject) {
		uiObjects.remove(renderedGuiObject);
	}

	/**
	 * Adds given gui object to userinterface.
	 * 
	 * @param renderedGuiObject
	 *            element.
	 */
	public void addElement(RenderedGuiObject renderedGuiObject) {
		uiObjects.add(renderedGuiObject);
	}

	/**
	 * Returns {@link GuiClickReactor} that reacts on click events from gui.
	 * 
	 * @return click reactor.
	 */
	public GuiClickReactor getClickReactor() {
		return reactor;
	}

	/**
	 * Returns the correspondend tooltip handler.
	 * 
	 * @return tooltip handler.
	 */
	public TooltipHandler getTooltipHandler() {
		return tooltipHandler;
	}

}
