package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Graphics2D;
import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * An element that is part of the gui and is shown to end user (that means it is
 * rendered). Gui objects can receive events and handle them if they override
 * event listeners.
 * 
 * @author illonis
 * 
 */
public abstract class RenderedGuiObject implements GameEventListener {

	private InformationProvider info;
	protected int screenX, screenY;

	/**
	 * Creates a new {@link RenderedGuiObject} using given game information.
	 * 
	 * @param info
	 *            game information to use.
	 */
	protected RenderedGuiObject(InformationProvider info) {
		this.info = info;
		screenX = screenY = 0;
	}

	/**
	 * Retrieves current game information.
	 * 
	 * @return game information.
	 */
	protected InformationProvider getInfo() {
		return info;
	}

	/**
	 * Renders this gui object on given graphics.
	 * 
	 * @param g2d
	 *            graphic target.
	 */
	public abstract void render(Graphics2D g2d);

	/**
	 * Indicates that gui has changed and interface positions have to be
	 * recalculated.
	 * 
	 * @param newWidth
	 *            new gui width.
	 * @param newHeight
	 *            new gui height.
	 */
	public abstract void onGuiSizeChanged(int newWidth, int newHeight);

	/**
	 * Indicates that player information have been received and initial logic is
	 * available.
	 */
	public abstract void onPlayerInformationReceived();

	@Override
	public void onNewObjectPosition(GameObject object) {
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos,
			int targetOwner) {
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
	}

	@Override
	public void onHealthChanged(int objectId, int newValue) {
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
	}

}
