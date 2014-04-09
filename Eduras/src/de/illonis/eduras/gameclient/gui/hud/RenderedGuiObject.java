package de.illonis.eduras.gameclient.gui.hud;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.Graphics;

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
import de.illonis.eduras.events.SetTeamResourceEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.gameclient.gui.game.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.game.TooltipHandler;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * An element that is part of the gui and is shown to end user (that means it is
 * rendered). Gui objects can receive events and handle them if they override
 * event listeners.<br>
 * Guielements can be selected to be invisible for spectators using
 * {@link #setVisibleForSpectator(boolean)}.
 * 
 * @author illonis
 * 
 */
public abstract class RenderedGuiObject implements GameEventListener {

	private UserInterface gui;
	protected float screenX, screenY;
	protected boolean visibleForSpectator;
	private final LinkedList<InteractMode> enabledModes;

	/**
	 * Creates a new {@link RenderedGuiObject} that is attached to given
	 * userinterface. The element is added to interface's guielement list
	 * automatically.
	 * 
	 * @param gui
	 *            userinterface this guielement should be attached to.
	 */
	protected RenderedGuiObject(UserInterface gui) {
		this.gui = gui;
		visibleForSpectator = false;
		enabledModes = new LinkedList<InteractMode>();
		screenX = screenY = 0;
		gui.addElement(this);
	}

	/**
	 * Returns whether this element is visible to spectators or not.
	 * 
	 * @return true if element is visible to spectators, false otherwise.
	 */
	public final boolean isVisibleForSpectator() {
		return visibleForSpectator;
	}

	/**
	 * Sets if this gui element is visible to spectators (default false).
	 * 
	 * @param visibleForSpectator
	 *            true if visible to spectators, false otherwise.
	 */
	protected final void setVisibleForSpectator(boolean visibleForSpectator) {
		this.visibleForSpectator = visibleForSpectator;
	}

	protected final TooltipHandler getTooltipHandler() {
		return gui.getTooltipHandler();
	}

	protected final void registerAsTooltipTriggerer(TooltipTriggerer triggerer) {
		gui.registerTooltipTriggerer(triggerer);
	}

	/**
	 * Returns {@link GuiClickReactor} that is responsible for event handling of
	 * assigned userinterface.
	 * 
	 * @return attached click reactor.
	 */
	protected final GuiClickReactor getMouseHandler() {
		return gui.getClickReactor();
	}

	/**
	 * Removes this gui object from userinterface it is attached to.
	 */
	public void removeFromGui() {
		gui.removeGuiElement(this);
	}

	/**
	 * Retrieves current game information.
	 * 
	 * @return game information.
	 */
	protected final InformationProvider getInfo() {
		return gui.getInfos();
	}

	/**
	 * Sets the interact modes in that this element is active and visible.
	 * 
	 * @param interactModes
	 *            one or more interact modes where this element should be active
	 *            in.
	 */
	protected final void setActiveInteractModes(InteractMode... interactModes) {
		enabledModes.clear();
		for (int i = 0; i < interactModes.length; i++) {
			enabledModes.add(interactModes[i]);
		}
	}

	/**
	 * Checks whether this hud object is enabled (aka visible) in given
	 * interaction mode. A disabled object will neither receive events nor be
	 * displayed.
	 * 
	 * @param interactMode
	 *            the interaction mode to check for.
	 * @return true if this element is enabled for given {@link InteractMode},
	 *         false otherwise.
	 */
	public final boolean isEnabledIn(InteractMode interactMode) {
		return enabledModes.isEmpty() || enabledModes.contains(interactMode);
	}

	/**
	 * Renders this gui object on given graphics.
	 * 
	 * @param g
	 *            graphic target.
	 */
	public abstract void render(Graphics g);

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
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
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

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
	}

	@Override
	public void onDeath(DeathEvent event) {
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
	}

	@Override
	public void onGameReady() {
	}

	@Override
	public void onVisibilityChanged(SetVisibilityEvent event) {
	}
	
	@Override
	public void onTeamResourceChanged(SetTeamResourceEvent setTeamResourceEvent) {
	}

}
