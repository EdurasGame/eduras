package de.illonis.eduras.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.Server;

/**
 * A client frame that connects to "localhost" and has a {@link CircleDrawPanel}
 * .
 * 
 * @author illonis
 * 
 */
public class ClientFrame extends JFrame {

	private final EdurasInitializer edurasInitializer;

	private final EventSender eventSender;
	private final InformationProvider informationProvider;
	private final NetworkManager networkManager;

	private static final long serialVersionUID = 1L;

	private CircleDrawPanel cdp;

	/**
	 * Creates a new clientframe assigned to given client.
	 * 
	 * @param client
	 *            client to assign to.
	 */
	public ClientFrame() {

		super("Eduras? Client");

		edurasInitializer = EdurasInitializer.getInstance();
		eventSender = edurasInitializer.getEventSender();
		informationProvider = edurasInitializer.getInformationProvider();
		networkManager = edurasInitializer.getNetworkManager();

		Dimension size = new Dimension(500, 400);
		setSize(size);
		setMinimumSize(size);

		buildInterface();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// center frame
		setLocationRelativeTo(null);

		try {
			networkManager.connect(InetAddress.getByName("localhost"),
					Server.DEFAULT_PORT);
		} catch (UnknownHostException e) {
			EduLog.passException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			EduLog.passException(e);
		}
		this.setVisible(true);
	}

	/**
	 * Builds interface
	 */
	private void buildInterface() {
		JPanel p = (JPanel) getContentPane();
		p.setLayout(new BorderLayout());
		cdp = new CircleDrawPanel(this);
		p.add(cdp, BorderLayout.CENTER);
	}

	/**
	 * Sends an event to server that indicates a mouse click.
	 * 
	 * @param x
	 *            x-Position of click
	 * @param y
	 *            y-Position of click
	 */
	public void sendMouseClick(int x, int y) {
		try {

			UserMovementEvent me = null;
			try {
				me = new UserMovementEvent(GameEventNumber.MOVE_LEFT_PRESSED,
						informationProvider.getPlayer().getId());
			} catch (ObjectNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			eventSender.sendEvent(me);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		} catch (WrongEventTypeException e) {
			EduLog.passException(e);
		}

	}

	/**
	 * Adds a new servercircle to drawpanel at given position.
	 * 
	 * @param d
	 *            x-position of new circle.
	 * @param e
	 *            x-position of new circle.
	 */
	public void newCircle(double d, double e) {
		cdp.addServerCircle(d, e);
		repaint();
	}

	public HashMap<Integer, GameObject> getObjects() {
		return informationProvider.getGameObjects();
	}
}
