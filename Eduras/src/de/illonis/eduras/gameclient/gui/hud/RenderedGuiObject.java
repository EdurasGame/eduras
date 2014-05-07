package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;

import org.newdawn.slick.Graphics;

import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameclient.gui.game.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.game.TooltipHandler;
import de.illonis.eduras.logicabstraction.InformationProvider;
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
public abstract class RenderedGuiObject extends GameEventAdapter {

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

}
