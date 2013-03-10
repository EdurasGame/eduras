package de.illonis.eduras.gameclient.gui;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.gameclient.GameClient;
import de.illonis.eduras.gameclient.NetworkEventReactor;
import de.illonis.eduras.gameclient.gui.guielements.GameStatBar;
import de.illonis.eduras.gameclient.gui.guielements.ItemDisplay;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.Vector2D;

public class ClientFrame extends JFrame implements NetworkEventReactor,
		ActionListener {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	private LoginPanel loginPanel;
	private ProgressPanel progressPanel;
	private final static String LOGINPANEL = "Login Card";
	private final static String CONNECTPANEL = "Connect Card";
	private final static String GAMEPANEL = "Game Card";
	private final GameClient client;
	private GamePanel gamePanel;
	private GameRenderer renderer;
	private RenderThread rendererThread;
	private ArrayList<RenderedGuiObject> uiObjects = new ArrayList<RenderedGuiObject>();
	private ItemDisplay itemDisplay;
	private GameStatBar statBar;
	private GuiNotifier notifier;
	private final GameCamera camera;
	private final CameraMouseListener cml;

	public ClientFrame(final GameClient client) {
		super("Eduras? Client");
		this.client = client;
		camera = new GameCamera();
		cml = new CameraMouseListener(camera);
		setSize(500, 500);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				client.onFocusLost();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client.tryExit();
			}

		});
		initGuiElements();
		buildGui();
	}

	private void initGuiElements() {

		uiObjects = new ArrayList<RenderedGuiObject>();
		notifier = new GuiNotifier(uiObjects);
		itemDisplay = new ItemDisplay(client, client.getInformationProvider());
		uiObjects.add(itemDisplay);
		statBar = new GameStatBar(client.getInformationProvider());
		uiObjects.add(statBar);
		client.registerTooltipTriggerer(itemDisplay);
		client.addClickableGuiElement(itemDisplay);
	}

	private void buildGui() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		loginPanel = new LoginPanel();
		progressPanel = new ProgressPanel(this, client.getNetworkManager());
		loginPanel.setActionListener(this);

		cardLayout.show(getContentPane(), LOGINPANEL);

		gamePanel = new GamePanel();
		client.addMouseListenersTo(gamePanel);
		gamePanel.addMouseMotionListener(cml);
		gamePanel.addMouseListener(cml);
		add(loginPanel, LOGINPANEL);
		add(progressPanel, CONNECTPANEL);
		add(gamePanel, GAMEPANEL);
		renderer = new GameRenderer(uiObjects, camera,
				client.getInformationProvider());

	}

	public GameRenderer getRenderer() {
		return renderer;
	}

	@Override
	public Point getLocationOnScreen() {
		if (gamePanel.isVisible())
			return gamePanel.getLocationOnScreen();
		return super.getLocationOnScreen();
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
		notifyGuiSizeChanged();
	}

	/**
	 * Notifies all ui objects that gui size has changed.
	 * 
	 */
	private void notifyGuiSizeChanged() {
		for (RenderedGuiObject obj : uiObjects) {
			obj.onGuiSizeChanged(gamePanel.getWidth(), gamePanel.getHeight());
		}
	}

	@Override
	public void onPlayerReceived() {
		// Notifies all ui objects that player data have been received.
		for (RenderedGuiObject obj : uiObjects) {
			obj.onPlayerInformationReceived();
		}
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
			camera.setSize(gamePanel.getWidth(), gamePanel.getHeight());
			EduLog.fine("[GUI] Size changed. New size: " + getWidth() + ", "
					+ getHeight());
			notifyGuiSizeChanged();
		}
	}

	@Override
	public void onConnected(int clientId) {
	}

	@Override
	public void onConnectionLost() {
		showProgress();
		progressPanel.setError("Connection lost.");
	}

	@Override
	public void onDisconnect() {
		cml.stop();
		if (rendererThread != null)
			rendererThread.stop();
		dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// fired when user clicked login.
		String clientName = loginPanel.getUserName();
		client.setClientName(clientName);
		if (clientName.length() < 3)
			return;

		showProgress();
		try {
			progressPanel.start(loginPanel.getAddress(), loginPanel.getPort());
		} catch (InvalidValueEnteredException e1) {
			progressPanel.setError("Invalid values entered.");
		}
	}

	@Override
	public void onGameReady() {
		rendererThread = new RenderThread(renderer, gamePanel);
		addComponentListener(new ResizeMonitor());
		camera.setSize(gamePanel.getWidth(), gamePanel.getHeight());
		Thread t = new Thread(rendererThread);
		t.start();
		client.addKeyHandlerTo(gamePanel);
		showGame();
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
		vec.modifyX(camera.getX());
		vec.modifyY(camera.getY());
		return vec;
	}

	/**
	 * Returns gui notifier that notifies gui elements.
	 * 
	 * @return gui notifier.
	 */
	public GuiNotifier getNotifier() {
		return notifier;
	}
}
