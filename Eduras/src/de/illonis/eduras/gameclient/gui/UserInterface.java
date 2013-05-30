package de.illonis.eduras.gameclient.gui;

import java.util.ArrayList;

import de.illonis.eduras.gameclient.GameEventReactor;
import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.gameclient.TooltipTriggererNotifier;
import de.illonis.eduras.gameclient.gui.guielements.GameModeBar;
import de.illonis.eduras.gameclient.gui.guielements.ItemDisplay;
import de.illonis.eduras.gameclient.gui.guielements.PlayerStatBar;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gameclient.gui.guielements.StatisticsWindow;
import de.illonis.eduras.gameclient.gui.guielements.TimeFrame;
import de.illonis.eduras.gameclient.gui.guielements.TooltipTriggerer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.networking.ServerClient.ClientRole;

/**
 * Holds all user interface elements and listens for game events and notifies
 * all gui elements.
 * 
 * @author illonis
 * 
 */
public class UserInterface implements GuiResizeListener, UserInputListener {
	private ArrayList<RenderedGuiObject> uiObjects;
	private InformationProvider infos;
	private GuiClickReactor reactor;
	private TooltipHandler tooltipHandler;
	private TooltipTriggererNotifier tooltipNotifier;
	private StatisticsWindow statWindow;
	private boolean spectator;

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
		spectator = false;
		this.reactor = clickReactor;
		this.tooltipNotifier = tooltipNotifier;
		createElements();
		EventListenerGui elg = new EventListenerGui(infos, uiObjects);
		infos.addEventListener(new GameEventReactor(elg));
	}

	private void createElements() {
		new ItemDisplay(this);
		new GameModeBar(this);
		new PlayerStatBar(this);
		new TimeFrame(this);
		statWindow = new StatisticsWindow(this);
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

	@Override
	public void showStatWindow() {
		statWindow.setVisible(true);
	}

	@Override
	public void hideStatWindow() {
		statWindow.setVisible(false);
	}

	/**
	 * Returns whether this is a spectator gui or not.
	 * 
	 * @return true if this gui is for a spectator.
	 */
	public boolean isSpectator() {
		return spectator;
	}

	void setRole(ClientRole role) {
		spectator = (role == ClientRole.SPECTATOR);
		if (spectator) {
			for (int i = 0; i < uiObjects.size(); i++) {
				RenderedGuiObject o = uiObjects.get(i);
				if (!o.isVisibleForSpectator())
					removeGuiElement(o);
			}
		}
	}

}
