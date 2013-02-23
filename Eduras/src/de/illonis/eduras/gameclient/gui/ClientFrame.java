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
import de.illonis.eduras.gameclient.gui.guielements.ItemDisplay;
import de.illonis.eduras.logger.EduLog;

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

	public ClientFrame(final GameClient client) {
		super("Eduras? Client");
		this.client = client;
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
		client.addMouseListenersTo(gamePanel);
		add(loginPanel, LOGINPANEL);
		add(progressPanel, CONNECTPANEL);
		add(gamePanel, GAMEPANEL);
		renderer = new GameRenderer(client, client.getCamera(),
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
	 * Notifies ui objects that gui size has changed.
	 */
	private void notifyGuiSizeChanged() {
		renderer.notifyGuiSizeChanged(gamePanel.getWidth(),
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
			client.getCamera().setSize(gamePanel.getWidth(),
					gamePanel.getHeight());
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

	public ItemDisplay getItemDisplay() {
		return renderer.getItemDisplay();
	}

	@Override
	public void onPlayerReceived() {
		renderer.notifyPlayerReceived();
	}

	@Override
	public void onGameReady() {
		rendererThread = new RenderThread(renderer, gamePanel);
		addComponentListener(new ResizeMonitor());
		client.getCamera().setSize(gamePanel.getWidth(), gamePanel.getHeight());
		Thread t = new Thread(rendererThread);
		t.start();
		client.addKeyHandlerTo(gamePanel);
		showGame();
	}
}
