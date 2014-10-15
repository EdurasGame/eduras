package de.illonis.eduras.gameclient.gui.hud;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.TimerTask;

import de.illonis.eduras.gameclient.ChatCache;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.HudNotifier;
import de.illonis.eduras.gameclient.gui.TimedTasksHolderGUI;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.game.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.game.GuiResizeListener;
import de.illonis.eduras.gameclient.gui.game.TooltipHandler;
import de.illonis.eduras.gameclient.gui.game.TooltipTriggererNotifier;
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
public class UserInterface implements GuiResizeListener {

	private static final long PING_TIME = 2000;

	private final LinkedList<RenderedGuiObject> uiObjects;
	private InformationProvider infos;
	private GuiClickReactor reactor;
	private TooltipHandler tooltipHandler;
	private TooltipTriggererNotifier tooltipNotifier;
	private StatisticsWindow statWindow;
	private DragSelectionRectangle dragRect;
	private boolean spectator;
	private NotificationPanel notificationPanel;
	private TipPanel tipPanel;
	private BigPanel bigPanel;
	private PingDisplay pingDisplay;
	private final ChatCache cache;
	private GameRenderer renderer;
	private ActionBar actionBar;
	private HudNotifier hudNotifier;
	private final GuiInternalEventListener guiReactor;
	private MiniMap minimap;
	private ExitPopup exitPopup;
	private SelectTeamPopup selectTeamPopup;
	private GamePanelLogic logic;
	private LinkedList<Cancelable> cancebleElements;

	public ActionBar getActionBar() {
		return actionBar;
	}

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
	 * @param reactor
	 *            the listener for guievents.
	 * @param cache
	 *            the chat cache object.
	 * @param logic
	 *            the logic
	 */
	public UserInterface(InformationProvider infos,
			TooltipTriggererNotifier tooltipNotifier,
			GuiClickReactor clickReactor, HudNotifier hudNotifier,
			GuiInternalEventListener reactor, ChatCache cache,
			GamePanelLogic logic) {
		this.uiObjects = new LinkedList<RenderedGuiObject>();
		this.infos = infos;
		this.hudNotifier = hudNotifier;
		spectator = false;
		this.guiReactor = reactor;
		this.reactor = clickReactor;
		this.tooltipNotifier = tooltipNotifier;
		this.cache = cache;
		this.logic = logic;
		this.cancebleElements = new LinkedList<Cancelable>();
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
		return renderer.takeScreenshot();
	}

	private void createElements() {
		new GameModeBar(this);
		new PlayerStatBar(this);
		actionBar = new ActionBar(this, guiReactor);
		new StrategyPanel(this);
		minimap = new MiniMap(this);
		new RemainingTimeFrame(this);
		new ItemDisplay(this, minimap);
		new RespawnTimeFrame(this);
		new ResourceDisplay(this);
		new BlinkDisplay(this, minimap);
		pingDisplay = new PingDisplay(this);
		notificationPanel = new NotificationPanel(this);
		tipPanel = new TipPanel(this, minimap);
		statWindow = new StatisticsWindow(this);
		bigPanel = new BigPanel(this);
		dragRect = new DragSelectionRectangle(this);
		exitPopup = new ExitPopup(this);
		selectTeamPopup = new SelectTeamPopup(this);
		cancebleElements.add(exitPopup);
		cancebleElements.add(selectTeamPopup);
		new ChatDisplay(cache, this);
		// new BugReportButton(this);

		actionBar.initPages(this, guiReactor);
	}

	GameCamera getGameCamera() {
		return renderer.getCamera();
	}

	public GamePanelLogic getLogic() {
		return logic;
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

	public void showStatWindow() {
		statWindow.setVisible(true);
	}

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
	 * Shows a notification to the user.
	 * 
	 * @param message
	 *            the message.
	 */
	public void showNotification(String message) {
		notificationPanel.addNotification(message);
	}

	/**
	 * Shows a tip at the action bar.
	 * 
	 * @param message
	 *            the message.
	 */
	public void showTip(String message) {
		tipPanel.setMessage(message);
	}

	/**
	 * Shows a big message.
	 * 
	 * @param message
	 *            the message.
	 */
	public void showBigMessage(String message) {
		bigPanel.setMessage(message);
	}

	public void showExitPopup() {
		exitPopup.setVisible(true);
	}

	/**
	 * Sets the game renderer.
	 * 
	 * @param renderer
	 *            the renderer.
	 */
	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
		minimap.setCamera(renderer.getCamera());
	}

	public GuiInternalEventListener getListener() {
		return guiReactor;
	}

	public void showTeamSelectDialogue() {
		selectTeamPopup.setVisible(true);
	}

	/**
	 * Called when the cancle button is pressed.
	 * 
	 * @return Returns true if one of the cancleable ui elements consumed the
	 *         cancle.
	 */
	public boolean onCancel() {
		for (Cancelable cancleableElement : cancebleElements) {
			if (cancleableElement.cancel()) {
				return true;
			}
		}
		return false;
	}
}
