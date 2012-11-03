package de.illonis.eduras.gui;

import java.net.InetAddress;

import javax.swing.JFrame;

import de.illonis.eduras.InputKeyHandler;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.NoValueEnteredException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
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
	private NetworkEventHandler eventHandler;

	private static final long serialVersionUID = 1L;

	public Gui() {
		loadTools();
		buildGui();
		connectDialog = new ConnectDialog(this);

	}

	private void buildGui() {

		gamePanel = new GamePanel();
		renderer = new GameRenderer(infoPro, gamePanel);
		rendererThread = new RenderThread(renderer);
		setContentPane(gamePanel);
		setSize(500, 500);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		} catch (NoValueEnteredException e) {
			System.out.println("empty");
			return;
		}
		ConnectProgressDialog cpd = new ConnectProgressDialog(this, nwm);
		cpd.start(address, port);
		System.out.println("closed");
		if (!cpd.isOK()) {
			System.out.println("err");
			showConnectDialog();
		}
	}

	/**
	 * Called when connection to server is established.
	 */
	void onConnected() {
		
		Thread t = new Thread(rendererThread);
		t.start();
		System.out.println("ownerid: " + infoPro.getOwnerID());
		keyHandler = new InputKeyHandler(infoPro.getOwnerID(), eventSender);
		gamePanel.addKeyListener(keyHandler);
		try {
			eventSender.sendEvent(new GameInfoRequest(infoPro.getOwnerID()));
		} catch (WrongEventTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}