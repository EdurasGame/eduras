package de.illonis.eduras.gui;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JFrame;

import de.illonis.eduras.InputKeyHandler;
import de.illonis.eduras.exceptions.NoValueEnteredException;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
/**
 * Graphical user interface for enduser.
 * @author illonis
 *
 */
public class Gui extends JFrame {

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private GamePanel gamePanel;
	private GameRenderer renderer;
	private RenderThread rt;
	private ConnectDialog cd;
	private InputKeyHandler keyHandler;
	private NetworkEventHandler eventHandler;

	private static final long serialVersionUID = 1L;

	public Gui() {
		loadTools();
		buildGui();
		cd = new ConnectDialog(this);
		eventHandler = new NetworkEventHandler(this);
		nwm.setNetworkEventListener(eventHandler);
	}

	private void buildGui() {

		gamePanel = new GamePanel();
		renderer = new GameRenderer(infoPro, gamePanel);
		rt = new RenderThread(renderer);
		setContentPane(gamePanel);
		setSize(500, 500);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void loadTools() {
		EdurasInitializer ei = EdurasInitializer.getInstance();
		eventSender = ei.getEventSender();
		infoPro = ei.getInformationProvider();
		nwm = ei.getNetworkManager();
	}

	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.setVisible(true);
		gui.showConnectDialog();
	}

	void showConnectDialog() {
		cd.setVisible(true);
		InetAddress address;
		int port;
		try {
			address = cd.getAddress();
			port = cd.getPort();
		} catch (NoValueEnteredException e) {
			System.out.println("empty");
			return;
		}
		try {
			nwm.connect(address, port);
		} catch (IOException e) {
			cd.setErrorMessage(e.getMessage());
			e.printStackTrace();
			showConnectDialog();
		}
	}

	/**
	 * Called when connection to server is established.
	 */
	void onConnected() {
		Thread t = new Thread(rt);
		t.start();
		keyHandler = new InputKeyHandler(infoPro.getOwnerID(), eventSender);
		gamePanel.addKeyListener(keyHandler);
	}
}
