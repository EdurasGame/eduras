package de.illonis.eduras.gameclient.gui;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.ConnectionEstablisher;
import de.illonis.eduras.gameclient.GameClient;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.animation.AnimationFactory;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;
import de.illonis.eduras.gameclient.gui.login.LoginPanelLogic;
import de.illonis.eduras.gameclient.gui.progress.LoadingPanelLogic;
import de.illonis.eduras.gameclient.gui.progress.ProgressPanelLogic;

/**
 * game panel and all other gui things.
 * 
 * @author illonis
 * 
 */
public class ClientFrame extends JFrame {

	private final static Logger L = EduLog.getLoggerFor(ClientFrame.class
			.getName());

	private static final long serialVersionUID = 1L;
	private final CardLayout cardLayout;
	private final LoginPanelLogic loginPanel;
	private final ProgressPanelLogic progressPanel;
	private final GamePanelLogic gamePanel;
	private final LoadingPanelLogic loadingPanel;

	private final static String LOGINPANEL = "Login Card";
	private final static String CONNECTPANEL = "Connect Card";
	private final static String LOADINGPANEL = "Loading Card";
	private final static String GAMEPANEL = "Game Card";
	protected final GameClient client;

	/**
	 * Creates a new clientframe.
	 * 
	 * @param client
	 *            associated client.
	 */
	public ClientFrame(final GameClient client) {
		super("Eduras? Client");
		this.client = client;

		setSize(800, 600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.onFocusLost();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client.tryExit();
			}

		});
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		GuiInternalEventListener guiEventListener = new GuiInternalEventListener(
				client);

		loginPanel = new LoginPanelLogic(guiEventListener);
		progressPanel = new ProgressPanelLogic(guiEventListener);
		loadingPanel = new LoadingPanelLogic(guiEventListener);
		gamePanel = new GamePanelLogic(guiEventListener, client.getData());

		getContentPane().add(loginPanel.getGui(), LOGINPANEL);
		getContentPane().add(progressPanel.getGui(), CONNECTPANEL);
		getContentPane().add(loadingPanel.getGui(), LOADINGPANEL);
		getContentPane().add(gamePanel.getGui(), GAMEPANEL);
		showLogin();
	}

	/**
	 * @return the game panel.
	 */
	public GamePanelLogic getGamePanel() {
		return gamePanel;
	}

	/**
	 * Indicates that a client connected to the game.
	 * 
	 * @param clientId
	 *            the id of the connected client.
	 */
	public void onClientConnected(int clientId) {
		if (client.getOwnerID() == clientId) {
			setTitle("Eduras? Client #" + clientId + " ("
					+ client.getClientName() + ")");
		}
	}

	/**
	 * Indicates that a client lost connection.
	 * 
	 * @param clientId
	 *            the id of the client that lost connection.
	 */
	public void onClientConnectionLost(int clientId) {
		if (clientId == client.getOwnerID()) {
			L.warning("Connection lost.");
			stopGame(true);
			setTitle("Eduras? Client");
		} else {
			// TODO: show message to user
		}
	}

	private void stopGame(boolean gracefully) {
		hideGame();
		showLogin();
		if (gracefully)
			JOptionPane.showMessageDialog(this, "Connection to server lost",
					"Connection lost", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Indicates that a client disconnected from the game.
	 * 
	 * @param clientId
	 *            the disconnected client.
	 * @param wantsExit
	 *            true when player wanted to exit the game, false otherwise.
	 */
	public void onClientDisconnect(int clientId, boolean wantsExit) {
		if (clientId == client.getOwnerID()) {
			stopGame(!wantsExit);
		}
	}

	/**
	 * Indicates that the game is ready to start and data should be preloaded
	 * now.
	 */
	public void startAndShowGame() {
		hideProgress();
		showLoading();
	}

	/**
	 * Triggered when data preloading has finished.
	 */
	public void onDataReady() {
		hideLoading();
		showGame();
	}

	/**
	 * shows loading panel.
	 */
	public void showLoading() {
		cardLayout.show(getContentPane(), LOADINGPANEL);
		loadingPanel.onShown();
	}

	/**
	 * hides loading panel.
	 */
	public void hideLoading() {
		loadingPanel.onHidden();
	}

	/**
	 * shows login panel.
	 */
	public void hideLogin() {
		loginPanel.onHidden();
	}

	/**
	 * Shows login panel.
	 */
	public void showLogin() {
		cardLayout.show(getContentPane(), LOGINPANEL);
		
		loginPanel.onShown();
	}

	/**
	 * Shows connect progress panel.
	 * 
	 * @param establisher
	 */
	public void showProgress(ConnectionEstablisher establisher) {
		cardLayout.show(getContentPane(), CONNECTPANEL);
		progressPanel.setEstablishThread(establisher);
		progressPanel.onShown();
	}

	/**
	 * Shows gamepanel. Also requests focus of gamepanel to be able to react to
	 * key strokes.
	 */
	public void showGame() {
		cardLayout.show(getContentPane(), GAMEPANEL);
		AnimationFactory.init(client.getData());
		gamePanel.onShown();
	}

	/**
	 * hides progress panel.
	 */
	public void hideProgress() {
		progressPanel.onHidden();
	}

	/**
	 * Terminates the client.
	 */
	public void onExit() {
		hideGame();
		hideLoading();
		hideProgress();
		hideLogin();
		dispose();
		System.exit(0);
	}

	private void hideGame() {
		setTitle("Eduras? Client");
		gamePanel.onHidden();
		AnimationFactory.dispose();
	}

	/**
	 * Sets the hud notifier.
	 * 
	 * @param hudNotifier
	 *            the hud notifier.
	 */
	public void setHudNotifier(HudNotifier hudNotifier) {
		gamePanel.setHudNotifier(hudNotifier);
	}

	/**
	 * Shows a notification.
	 * 
	 * @param msg
	 *            the message.
	 */
	public void notification(String msg) {
		gamePanel.showNotification(msg);
	}

}
