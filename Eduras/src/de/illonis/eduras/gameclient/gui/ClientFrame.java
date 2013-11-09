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
import de.illonis.eduras.gameclient.GuiEventListener;
import de.illonis.eduras.gameclient.NetworkEventReactor;
import de.illonis.eduras.gameclient.gui.game.GamePanel;
import de.illonis.eduras.gameclient.gui.login.LoginPanel;
import de.illonis.eduras.gameclient.gui.progress.LoadingPanel;
import de.illonis.eduras.gameclient.gui.progress.ProgressPanel;

/**
 * game panel and all other gui things.
 * 
 * @author illonis
 * 
 */
public class ClientFrame extends JFrame implements NetworkEventReactor {

	private final static Logger L = EduLog.getLoggerFor(ClientFrame.class
			.getName());

	private static final long serialVersionUID = 1L;
	private final CardLayout cardLayout;
	private final LoginPanel loginPanel;
	private final ProgressPanel progressPanel;
	private final GamePanel gamePanel;
	private final LoadingPanel loadingPanel;

	private final static String LOGINPANEL = "Login Card";
	private final static String CONNECTPANEL = "Connect Card";
	private final static String LOADINGPANEL = "Loading Card";
	private final static String GAMEPANEL = "Game Card";
	protected final GameClient client;
	private final GuiEventListener guiEventListener;

	/**
	 * Creates a new clientframe.
	 * 
	 * @param client
	 *            associated client.
	 */
	public ClientFrame(final GameClient client) {
		super("Eduras? Client");
		this.client = client;

		guiEventListener = new GuiEventListener(client);
		setSize(500, 500);
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
		loginPanel = new LoginPanel(guiEventListener);
		progressPanel = new ProgressPanel(guiEventListener);
		loadingPanel = new LoadingPanel(guiEventListener);
		gamePanel = new GamePanel(guiEventListener);

		getContentPane().add(loginPanel.getGui(), LOGINPANEL);
		getContentPane().add(progressPanel.getGui(), CONNECTPANEL);
		getContentPane().add(loadingPanel.getGui(), LOADINGPANEL);
		getContentPane().add(gamePanel.getGui(), GAMEPANEL);
		showLogin();
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
			L.warning("Connection lost.");
			stopGame(true);
			setTitle("Eduras? Client");
		}
	}

	private void stopGame(boolean gracefully) {
		if (gracefully)
			JOptionPane.showMessageDialog(this, "Connection to server lost",
					"Connection lost", JOptionPane.ERROR_MESSAGE);
		gamePanel.onHidden();
		showLogin();
	}

	@Override
	public void onDisconnect(int clientId) {
		if (clientId == client.getOwnerID()) {
			stopGame(false);
		}
	}

	@Override
	public void onGameReady() {
		hideProgress();
		showLoading();
	}

	public void onDataReady() {
		hideLoading();
		showGame();
	}

	public void showLoading() {
		cardLayout.show(getContentPane(), LOADINGPANEL);
		loadingPanel.onShown();
	}

	public void hideLoading() {
		loadingPanel.onHidden();
	}

	@Override
	public void onPlayerReceived() {
		// nothing to do here because object factory notices guinotifier.
	}

	@Override
	public void onUDPReady(int clientId) {
		// do nothing
	}

	public void hideLogin() {
		loginPanel.onHidden();
		client.stopDiscovery();
	}

	/**
	 * Shows login panel.
	 */
	public void showLogin() {
		System.out.println("show login");
		cardLayout.show(getContentPane(), LOGINPANEL);
		client.startDiscovery(loginPanel);
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
		gamePanel.onShown();
	}

	public void hideProgress() {
		progressPanel.onHidden();
	}

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

	}

}
