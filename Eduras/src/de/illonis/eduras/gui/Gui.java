package de.illonis.eduras.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.illonis.eduras.InputKeyHandler;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.locale.Localization;
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
public class Gui extends JFrame {

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private GamePanel gamePanel;
	private GameRenderer renderer;
	private RenderThread rendererThread;
	private final ConnectDialog connectDialog;
	private InputKeyHandler keyHandler;
	private String clientName;
	private NetworkEventHandler eventHandler;
	private EdurasInitializer initializer;
	private Settings settings;
	private final GameCamera camera;
	private final CameraMouseListener cml;

	private static final long serialVersionUID = 1L;

	private Gui() {
		super("Eduras? Client");
		loadTools();
		camera = new GameCamera();
		cml = new CameraMouseListener(camera);

		addComponentListener(new ResizeMonitor());
		buildGui();
		connectDialog = new ConnectDialog(this);
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

		gamePanel = new GamePanel();
		gamePanel.addMouseMotionListener(cml);
		gamePanel.addMouseListener(cml);
		renderer = new GameRenderer(camera, infoPro);
		rendererThread = new RenderThread(renderer, gamePanel);
		getContentPane().add(gamePanel);
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
		Gui gui = new Gui();
		gui.setVisible(true);

		// welcome to devil's loop
		while (!gui.showConnectDialog())
			;
	}

	/**
	 * Shows connection dialog that asks for user input.
	 * 
	 * @return true if user input was valid, false otherwise.
	 */
	private boolean showConnectDialog() {
		connectDialog.setVisible(true);
		InetAddress address;
		int port;
		if (connectDialog.isAborted())
			return true;
		try {
			address = connectDialog.getAddress();
			port = connectDialog.getPort();
			clientName = connectDialog.getUserName();
		} catch (InvalidValueEnteredException e) {
			return false;
		}
		ConnectProgressDialog cpd = new ConnectProgressDialog(this, nwm);
		cpd.start(address, port);
		return cpd.isOK();
	}

	/**
	 * Called when connection to server is established.
	 */
	void onConnected() {

		camera.setSize(getWidth(), getHeight());
		Thread t = new Thread(rendererThread);
		t.start();
		System.out.println("[CLIENT] Connected. OwnerId: "
				+ infoPro.getOwnerID());
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
			e.printStackTrace();
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
			System.out.println("[GUI] Size changed. New size: " + getWidth()
					+ ", " + getHeight());
		}
	}
}