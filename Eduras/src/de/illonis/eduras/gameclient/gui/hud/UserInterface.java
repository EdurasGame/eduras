package de.illonis.eduras.gameclient.gui.hud;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.TimerTask;

import de.illonis.eduras.gameclient.ChatCache;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.TimedTasksHolderGUI;
import de.illonis.eduras.gameclient.gui.game.FPSListener;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.game.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.game.GuiResizeListener;
import de.illonis.eduras.gameclient.gui.game.TooltipHandler;
import de.illonis.eduras.gameclient.gui.game.TooltipTriggererNotifier;
import de.illonis.eduras.gameclient.gui.game.UserInputListener;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.networking.ClientRole;

/**
 * Holds all user interface elements and listens for game events and notifies
 * all gui elements.
 * 
 * @author illonis
 * 
 */
public class UserInterface implements GuiResizeListener, UserInputListener {

	private static final long PING_TIME = 2000;

	private final LinkedList<RenderedGuiObject> uiObjects;
	private InformationProvider infos;
	private GuiClickReactor reactor;
	private TooltipHandler tooltipHandler;
	private TooltipTriggererNotifier tooltipNotifier;
	private StatisticsWindow statWindow;
	private FPSDisplay fpsDisplay;
	private DragSelectionRectangle dragRect;
	private boolean spectator;
	private NotificationPanel notificationPanel;
	private PingDisplay pingDisplay;
	private final ChatCache cache;
	private GameRenderer renderer;

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
	 * @param hudNotifier
	 *            the hud notifier.
	 * @param cache
	 *            the chat cache object.
	 */
	public UserInterface(InformationProvider infos,
			TooltipTriggererNotifier tooltipNotifier,
			GuiClickReactor clickReactor, HudNotifier hudNotifier,
			ChatCache cache) {
		this.uiObjects = new LinkedList<RenderedGuiObject>();
		this.infos = infos;
		spectator = false;
		this.reactor = clickReactor;
		this.tooltipNotifier = tooltipNotifier;
		this.cache = cache;
		createElements();
		hudNotifier.setUiObjects(this.uiObjects);

		TimerTask pingRequester = new TimerTask() {

			@Override
			public void run() {
				EdurasInitializer.getInstance().getNetworkManager().ping();
			}
		};
		TimedTasksHolderGUI.getInstance().addTask(pingRequester, PING_TIME);
	}

	/**
	 * @return the gamePanel.
	 */
	public BufferedImage getScreenshot() {
		return renderer.getScreenshot();
	}

	private void createElements() {
		new ItemDisplay(this);
		new GameModeBar(this);
		new PlayerStatBar(this);
		new TimeFrame(this);
		fpsDisplay = new FPSDisplay(this);
		pingDisplay = new PingDisplay(this);
		notificationPanel = new NotificationPanel(this);
		dragRect = new DragSelectionRectangle(this);
		statWindow = new StatisticsWindow(this);
		new ChatDisplay(cache, this);
		new BugReportButton(this);
	}

	/**
	 * Returns informationprovider.
	 * 
	 * @return informationprovider.
	 */
	public InformationProvider getInfos() {
		return infos;
	}

	/**
	 * Returns the UI object of the user interface.
	 * 
	 * @return The UI objects in a list.
	 */
	public LinkedList<RenderedGuiObject> getUiObjects() {
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
	public void setTooltipHandler(TooltipHandler h) {
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

	/**
	 * @return the element that listens for fps.
	 */
	public FPSListener getFPSListener() {
		return fpsDisplay;
	}

	/**
	 * @return the element that listens for ping.
	 */
	public PingListener getPingListener() {
		return pingDisplay;
	}

	/**
	 * @return the selection rectangle.
	 */
	public DragSelectionRectangle getDragRect() {
		return dragRect;
	}

	/**
	 * @param message
	 *            the message.
	 */
	public void showNotification(String message) {
		notificationPanel.addNotification(message);
	}

	@Override
	public void onChatEnter() {
	}

	@Override
	public boolean abortChat() {
		return false;
	}

	/**
	 * Sets the game renderer.
	 * 
	 * @param renderer
	 *            the renderer.
	 */
	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}
}
