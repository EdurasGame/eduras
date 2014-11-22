package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.gui.game.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.game.TooltipHandler;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.units.InteractMode;

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
public abstract class RenderedGuiObject extends GameEventAdapter implements
		Comparable<RenderedGuiObject> {

	private UserInterface gui;
	protected float screenX, screenY;
	protected boolean visibleForSpectator;
	private final LinkedList<InteractMode> enabledModes;
	private boolean visible;
	protected int zIndex;

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
		visible = true;
		gui.addElement(this);
		zIndex = 0;
	}

	protected void setLocation(float x, float y) {
		this.screenX = x;
		this.screenY = y;
	}

	/**
	 * Returns whether this element is visible to spectators or not.
	 * 
	 * @return true if element is visible to spectators, false otherwise.
	 */
	public final boolean isVisibleForSpectator() {
		return visibleForSpectator;
	}

	protected final InteractMode getCurrentMode()
			throws ObjectNotFoundException {
		if (getInfo().getClientData().getRole() == ClientRole.SPECTATOR) {
			return InteractMode.MODE_SPECTATOR;
		} else {
			return getInfo().getPlayer().getCurrentMode();
		}
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

	protected final List<InteractMode> getActiveInteractModes() {
		return new LinkedList<InteractMode>(enabledModes);
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
	public final boolean isEnabledInInteractMode(InteractMode interactMode) {
		if (interactMode == InteractMode.MODE_SPECTATOR)
			return isVisibleForSpectator();
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
	 * Tells whether this GUI object shall be rendered in the given mode. The
	 * default is true in all modes. An inheriting GUI object can overwrite
	 * this.
	 * 
	 * @param gameMode
	 * @return true if it shall be enabled
	 */
	public boolean isEnabledInGameMode(GameMode gameMode) {
		return true;
	}

	/**
	 * Sets if this GUI object is visible.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Tells whether this gui object is set to be visible.
	 * 
	 * @return true if visible
	 */
	public boolean isVisible() {
		return visible;
	}

	@Override
	public int compareTo(RenderedGuiObject o) {
		return this.zIndex - o.zIndex;
	}
}
