package de.illonis.eduras.gameclient.gui;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.gameclient.GameClient;
import de.illonis.eduras.gameclient.NetworkEventReactor;
import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.Vector2D;

/**
 * The client frame that holds game panel and all other gui things.
 * 
 * @author illonis
 * 
 */
public class ClientFrame extends JFrame implements NetworkEventReactor,
		ActionListener, UserInputListener {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	private LoginPanel loginPanel;
	private ProgressPanel progressPanel;
	private final static String LOGINPANEL = "Login Card";
	private final static String CONNECTPANEL = "Connect Card";
	private final static String GAMEPANEL = "Game Card";
	protected final GameClient client;
	private GamePanel gamePanel;
	private UserInterface userInterface;
	private final GameCamera camera;
	private final CameraMouseListener cml;

	/**
	 * Creates a new clientframe.
	 * 
	 * @param client
	 *            associated client.
	 */
	public ClientFrame(final GameClient client) {
		super("Eduras? Client");
		this.client = client;
		camera = new GameCamera();
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
		cml = new CameraMouseListener(camera);

		buildGui();
	}

	private void buildGui() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		loginPanel = new LoginPanel();
		progressPanel = new ProgressPanel(this, client.getNetworkManager());
		loginPanel.setActionListener(this);

		cardLayout.show(getContentPane(), LOGINPANEL);

		gamePanel = new GamePanel();
		gamePanel.addMouseMotionListener(cml);
		gamePanel.addMouseListener(cml);
		client.addMouseListenersTo(gamePanel);
		addComponentListener(new ResizeMonitor());

		getContentPane().add(loginPanel, LOGINPANEL);
		getContentPane().add(progressPanel, CONNECTPANEL);
		getContentPane().add(gamePanel, GAMEPANEL);
	}

	/**
	 * Initializes user interface.
	 */
	public void initUserInterface() {
		userInterface = new UserInterface(client.getInformationProvider(),
				client, client);
		gamePanel.initRenderer(camera, userInterface,
				client.getInformationProvider());
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
		notifyGuiSizeChanged();
		gamePanel.requestFocus();
		gamePanel.requestFocusInWindow();
	}

	/**
	 * Notifies all ui objects that gui size has changed.
	 * 
	 */
	private void notifyGuiSizeChanged() {

		userInterface.onGuiSizeChanged(gamePanel.getWidth(),
				gamePanel.getHeight());
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

			EduLog.fine("[GUI] Size changed. New size: " + getWidth() + ", "
					+ getHeight());
			notifyGuiSizeChanged();
		}
	}

	@Override
	public void onConnected(int clientId) {
		if (client.getOwnerID() == clientId) {
			setTitle("Eduras? Client #" + clientId + " ("
					+ client.getClientName() + ")");
		}
	}

	@Override
	public void onConnectionLost(int clientId) {
		if (clientId == client.getOwnerID()) {
			EduLog.warning("Connection lost.");
			gamePanel.stopRendering();
			cml.stop();
			showProgress();
			progressPanel.setError("Connection lost.");
			setTitle("Eduras? Client");
		}
	}

	@Override
	public void onDisconnect(int clientId) {
		if (clientId == client.getOwnerID()) {
			gamePanel.stopRendering();
			cml.stop();
			dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// fired when user clicked login.
		loginAction();
	}

	private void loginAction() {
		String clientName = loginPanel.getUserName();
		client.setClientName(clientName);
		client.setRole(loginPanel.getRole());
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
		gamePanel.startRendering();
		camera.reset();

		client.addKeyHandlerTo(gamePanel);
		camera.setSize(gamePanel.getWidth(), gamePanel.getHeight());
		cml.start();
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
		double scale = gamePanel.getCurrentScale();
		Vector2D vec = new Vector2D(v);
		vec.modifyX(camera.getX() * scale);
		vec.modifyY(camera.getY() * scale);
		vec.mult(1 / scale);
		return vec;
	}

	/**
	 * Returns tooltip handler that handles tooltips.
	 * 
	 * @return tooltip handler.
	 */
	public TooltipHandler getTooltipHandler() {
		return userInterface.getTooltipHandler();
	}

	@Override
	public void onPlayerReceived() {
		// nothing to do here because object factory notices guinotifier.
	}

	@Override
	public void showStatWindow() {
		userInterface.showStatWindow();
	}

	@Override
	public void hideStatWindow() {
		userInterface.hideStatWindow();
	}
}
