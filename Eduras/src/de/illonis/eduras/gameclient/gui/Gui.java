package de.illonis.eduras.gameclient.gui;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.naming.InvalidNameException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gui.guielements.GuiClickReactor;
import de.illonis.eduras.gui.renderer.GameRenderer;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logger.EduLog.LogMode;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.Settings;

/**
 * Graphical user interface for enduser.
 * 
 * @author illonis
 * 
 */
public class Gui extends JFrame implements ActionListener {

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private GamePanel gamePanel;
	private GameRenderer renderer;
	private RenderThread rendererThread;
	private InputKeyHandler keyHandler;
	private String clientName;
	private NetworkEventHandler eventHandler;
	private EdurasInitializer initializer;
	private Settings settings;
	private final GameCamera camera;
	private final CameraMouseListener cml;
	private CardLayout cardLayout;
	private LoginPanel loginPanel;
	private ProgressPanel progressPanel;
	private final static String LOGINPANEL = "Login Card";
	final static String CONNECTPANEL = "Connect Card";
	final static String GAMEPANEL = "Game Card";
	private ClickState currentClickState;
	private LinkedList<GuiClickReactor> clickListeners;
	private int currentItemSelected = -1;

	private enum ClickState {
		DEFAULT, ITEM_SELECTED;
	}

	private static final long serialVersionUID = 1L;

	public Gui() {
		super("Eduras? Client");
		clickListeners = new LinkedList<GuiClickReactor>();
		loadTools();

		camera = new GameCamera();
		cml = new CameraMouseListener(camera);

		addComponentListener(new ResizeMonitor());
		buildGui();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				tryExit();
			}
		});
		currentClickState = ClickState.DEFAULT;
	}

	private void tryExit() {

		int result = JOptionPane.showConfirmDialog(this,
				Localization.getString("Client.exitconfirm"), "Exiting",
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			disconnect();
			dispose();
		}
	}

	private void buildGui() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		loginPanel = new LoginPanel();
		progressPanel = new ProgressPanel(this, nwm);
		loginPanel.setActionListener(this);

		cardLayout.show(getContentPane(), LOGINPANEL);
		renderer = new GameRenderer(this, camera, infoPro);
		gamePanel = new GamePanel();
		gamePanel.addMouseMotionListener(cml);
		gamePanel.addMouseListener(cml);
		ClickListener cl = new ClickListener();
		gamePanel.addMouseListener(cl);

		add(loginPanel, LOGINPANEL);
		add(progressPanel, CONNECTPANEL);
		add(gamePanel, GAMEPANEL);

		setSize(500, 500);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void loadTools() {
		initializer = EdurasInitializer.getInstance();
		eventSender = initializer.getEventSender();
		infoPro = initializer.getInformationProvider();
		eventHandler = new NetworkEventHandler(this);

		nwm = initializer.getNetworkManager();
		nwm.setNetworkEventListener(eventHandler);
	}

	/**
	 * Sends a game event to server using eventSender.
	 * 
	 * @param e
	 *            game event to send.
	 */
	void sendEvent(GameEvent e) {
		try {
			eventSender.sendEvent(e);
		} catch (WrongEventTypeException e1) {
			EduLog.passException(e1);
		} catch (MessageNotSupportedException e1) {
			EduLog.passException(e1);
		}
	}

	/**
	 * Called when connection to server is established.
	 */
	void onConnected() {
		showGame();
		rendererThread = new RenderThread(renderer, gamePanel);
		camera.setSize(getWidth(), getHeight());
		Thread t = new Thread(rendererThread);
		t.start();
		EduLog.info("[CLIENT] Connected. OwnerId: " + infoPro.getOwnerID());
		setTitle(getTitle() + " #" + infoPro.getOwnerID() + " (" + clientName
				+ ")");
		settings = initializer.getSettings();
		keyHandler = new InputKeyHandler(infoPro.getOwnerID(), eventSender,
				settings);
		gamePanel.addKeyListener(keyHandler);
		try {
			eventSender.sendEvent(new ClientRenameEvent(infoPro.getOwnerID(),
					clientName));
			eventSender.sendEvent(new GameInfoRequest(infoPro.getOwnerID()));

		} catch (WrongEventTypeException e) {
			EduLog.passException(e);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		} catch (InvalidNameException e) {
			EduLog.passException(e);
		}
	}

	/**
	 * Sends disconnect and stop-messages to all running threads and listeners.
	 */
	private void disconnect() {
		cml.stop();
		if (rendererThread != null)
			rendererThread.stop();
		initializer.shutdown();
		nwm.disconnect();
	}

	/**
	 * Resizes camera on frame size change.
	 * 
	 * @author illonis
	 * 
	 */
	private class ResizeMonitor extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			camera.setSize(getWidth(), getHeight());
			EduLog.fine("[GUI] Size changed. New size: " + getWidth() + ", "
					+ getHeight());
			renderer.notifyGuiSizeChanged(getWidth(), getHeight());
		}
	}

	/**
	 * Listens for click on gui and passes them to gui elements.
	 * 
	 * @author illonis
	 * 
	 */
	private class ClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			EduLog.info("Click at " + e.getX() + ", " + e.getY());
			Point p = e.getPoint();
			switch (currentClickState) {
			case DEFAULT:
				for (Iterator<GuiClickReactor> iterator = clickListeners
						.iterator(); iterator.hasNext();) {
					GuiClickReactor reactor = iterator.next();
					if (reactor.onClick(p))
						return;
				}
				inGameClick(p);
				break;
			case ITEM_SELECTED:
				itemUsed(currentItemSelected, new Vector2D(p));
				currentClickState = ClickState.DEFAULT;
				break;
			default:
				break;

			}
		}
	}

	/**
	 * Indicates a click into game world.<br>
	 * Note that position information is gui-relative and has to be computed to
	 * game coordinates.
	 * 
	 * @param p
	 *            click position in gui.
	 */
	private void inGameClick(Point p) {
		// TODO: implement
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// fired when user clicked login.
		clientName = loginPanel.getUserName();
		if (clientName.length() < 3)
			return;
		showProgress();
		try {
			progressPanel.start(loginPanel.getAddress(), loginPanel.getPort());
		} catch (InvalidValueEnteredException e1) {
			progressPanel.setError("Invalid values entered.");
		}
	}

	/**
	 * Shows login panel.
	 */
	public void showLogin() {
		cardLayout.show(getContentPane(), LOGINPANEL);
	}

	/**
	 * Shows connect progress panel.
	 */
	public void showProgress() {
		progressPanel.reset();
		cardLayout.show(getContentPane(), CONNECTPANEL);
	}

	/**
	 * Shows gamepanel. Also requests focus of gamepanel to be able to react to
	 * key strokes.
	 */
	public void showGame() {
		cardLayout.show(getContentPane(), GAMEPANEL);
		gamePanel.requestFocus();
		renderer.notifyGuiSizeChanged(getWidth(), getHeight());
	}

	/**
	 * Called when network connection is lost to display error message.
	 */
	public void networkLost() {
		showProgress();
		progressPanel.setError("Connection lost.");
	}

	/**
	 * Sends an item use event to server.
	 * 
	 * @param i
	 *            item slot.
	 * @param target
	 *            target position
	 */
	private void itemUsed(int i, Vector2D target) {
		ItemEvent event = new ItemEvent(GameEventNumber.ITEM_USE,
				infoPro.getOwnerID(), i);
		event.setTarget(computeGuiPointToGameCoordinate(target));
		sendEvent(event);
	}

	/**
	 * Computes a point that is relative to gui into game coordinates.
	 * 
	 * @param v
	 *            point to convert.
	 * @return game-coordinate point.
	 */
	public Vector2D computeGuiPointToGameCoordinate(Vector2D v) {
		Vector2D vec = new Vector2D(v);
		v.modifyX(-camera.getX());
		v.modifyY(-camera.getY());
		return vec;
	}

	/**
	 * Notifies gui that an item has been clicked so it can react on next click.
	 * 
	 * @param i
	 *            item slot clicked.
	 */
	public void itemClicked(int i) {
		if (i >= 0 && i < Inventory.MAX_CAPACITY) {
			currentItemSelected = i;
			currentClickState = ClickState.ITEM_SELECTED;
		} else {
			currentClickState = ClickState.DEFAULT;
			currentItemSelected = -1;
		}
	}

	/**
	 * Adds given listener to listener list.
	 * 
	 * @param l
	 *            new listener.
	 */
	public void addClickListener(GuiClickReactor l) {
		clickListeners.add(l);
	}
}