package de.illonis.eduras.gui;

import javax.swing.JFrame;

import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;

public class Gui extends JFrame {

	private InformationProvider infoPro;
	private EventSender eventSender;
	private NetworkManager nwm;
	private GamePanel gamePanel;
	private GameRenderer renderer;
	private RenderThread rt;

	private static final long serialVersionUID = 1L;

	public Gui() {
		loadTools();
		buildGui();

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
		ConnectDialog cd = new ConnectDialog(gui);
		cd.setVisible(true);
	}

	/**
	 * Called when connection to server is established.
	 */
	void onConnected() {
		Thread t = new Thread(rt);
		t.start();
	}
}
