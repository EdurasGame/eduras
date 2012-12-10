package de.illonis.eduras.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.naming.InvalidNameException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gui.renderer.GameRenderer;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logger.EduLog.LogMode;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
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

	final static String LOGINPANEL = "Login Card";
	final static String CONNECTPANEL = "Connect Card";
	final static String GAMEPANEL = "Game Card";

	private static final long serialVersionUID = 1L;

	private Gui() {
		super("Eduras? Client");
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
		renderer = new GameRenderer(camera, infoPro);
		gamePanel = new GamePanel();
		gamePanel.addMouseMotionListener(cml);
		gamePanel.addMouseListener(cml);

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

	public static void main(String[] args) {
		// new LoggerGui().setVisible(true);
		EduLog.setLogOutput(LogMode.CONSOLE);
		Gui gui = new Gui();
		gui.setVisible(true);
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
	 * Shows gamepanel.
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
}