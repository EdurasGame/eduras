package de.illonis.eduras.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.illonis.eduras.InputKeyHandler;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.NoValueEnteredException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;

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
	private ConnectDialog connectDialog;
	private InputKeyHandler keyHandler;
	private String clientName;
	private NetworkEventHandler eventHandler;

	private static final long serialVersionUID = 1L;

	public Gui() {
		super("Eduras? Client");
		loadTools();
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
		renderer = new GameRenderer(infoPro);
		rendererThread = new RenderThread(renderer, gamePanel);
		setContentPane(gamePanel);
		setSize(500, 500);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void loadTools() {
		EdurasInitializer ei = EdurasInitializer.getInstance();
		eventSender = ei.getEventSender();
		infoPro = ei.getInformationProvider();
		eventHandler = new NetworkEventHandler(this);

		nwm = ei.getNetworkManager();
		nwm.setNetworkEventListener(eventHandler);
	}

	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.setVisible(true);
		gui.showConnectDialog();
	}

	void showConnectDialog() {
		connectDialog.setVisible(true);
		InetAddress address;
		int port;
		try {
			address = connectDialog.getAddress();
			port = connectDialog.getPort();
			clientName = connectDialog.getUserName();
		} catch (NoValueEnteredException e) {
			return;
		}
		ConnectProgressDialog cpd = new ConnectProgressDialog(this, nwm);
		cpd.start(address, port);
		if (!cpd.isOK()) {
			System.err.println("Invalid input. Asking again");
			showConnectDialog();
		}
	}

	/**
	 * Called when connection to server is established.
	 */
	void onConnected() {

		Thread t = new Thread(rendererThread);
		t.start();
		System.out.println("[CLIENT] Connected. Ownerid: "
				+ infoPro.getOwnerID());
		setTitle(getTitle() + " #" + infoPro.getOwnerID());
		keyHandler = new InputKeyHandler(infoPro.getOwnerID(), eventSender);
		gamePanel.addKeyListener(keyHandler);
		try {
			eventSender.sendEvent(new GameInfoRequest(infoPro.getOwnerID()));
			eventSender.sendEvent(new ClientRenameEvent(infoPro.getOwnerID(),
					clientName));
		} catch (WrongEventTypeException e) {
			e.printStackTrace();
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private void disconnect() {

	}
}