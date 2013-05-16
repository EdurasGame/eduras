package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;

import de.illonis.eduras.gameclient.GameEventReactor;
import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.gameclient.TooltipTriggererNotifier;
import de.illonis.eduras.gameclient.gui.guielements.GameModeBar;
import de.illonis.eduras.gameclient.gui.guielements.ItemDisplay;
import de.illonis.eduras.gameclient.gui.guielements.PlayerStatBar;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gameclient.gui.guielements.TooltipTriggerer;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Holds all user interface elements and listens for game events and notifies
 * all gui elements.
 * 
 * @author illonis
 * 
 */
public class UserInterface implements GuiResizeListener {
	private ArrayList<RenderedGuiObject> uiObjects;
	private InformationProvider infos;
	private GuiClickReactor reactor;
	private TooltipHandler tooltipHandler;
	private TooltipTriggererNotifier tooltipNotifier;

	/**
	 * Creates the user interface. The tooltip handler will be set manually
	 * later on using {@link #setTooltipHandler(TooltipHandler)}.
	 * 
	 * @param infos
	 *            information.
	 * @param tooltipNotifier
	 *            tooltip notifier.
	 * @param clickReactor
	 *            click reactor.
	 */
	UserInterface(InformationProvider infos,
			TooltipTriggererNotifier tooltipNotifier,
			GuiClickReactor clickReactor) {
		this.uiObjects = new ArrayList<RenderedGuiObject>();
		this.infos = infos;
		this.reactor = clickReactor;
		this.tooltipNotifier = tooltipNotifier;
		createElements();
		EventListenerGui elg = new EventListenerGui(infos);
		infos.addEventListener(new GameEventReactor(elg));
	}

	private void createElements() {
		new ItemDisplay(this);
		new GameModeBar(this);
		new PlayerStatBar(this);
	}

	/**
	 * Returns informationprovider.
	 * 
	 * @return informationprovider.
	 */
	public InformationProvider getInfos() {
		return infos;
	}

	ArrayList<RenderedGuiObject> getUiObjects() {
		return uiObjects;
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

	/**
	 * Registers given triggerer to tooltip notifier.
	 * 
	 * @param triggerer
	 *            trigger.
	 */
	public void registerTooltipTriggerer(TooltipTriggerer triggerer) {
		tooltipNotifier.registerTooltipTriggerer(triggerer);
	}

	/**
	 * Sets tooltip handler to given tooltiphandler.
	 * 
	 * @param h
	 *            new tooltip handler.
	 */
	void setTooltipHandler(TooltipHandler h) {
		this.tooltipHandler = h;
	}

	@Override
	public void onGuiSizeChanged(int width, int height) {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onGuiSizeChanged(width, height);
		}
	}

}
