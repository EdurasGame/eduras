package de.illonis.eduras;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.networking.Client;
import de.illonis.eduras.networking.NetworkMessageSerializer;

/**
 * A client frame that connects to "localhost" and has a {@link CircleDrawPanel}
 * .
 * 
 * @author illonis
 * 
 */
public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Client client;

	private CircleDrawPanel cdp;

	/**
	 * Creates a new clientframe assigned to given client.
	 * 
	 * @param client
	 *            client to assign to.
	 */
	public ClientFrame(Client client) {
		super("Eduras? Client");
		this.client = client;

		Dimension size = new Dimension(500, 400);
		setSize(size);
		setMinimumSize(size);

		buildInterface();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// center frame
		setLocationRelativeTo(null);

		try {
			client.connect(InetAddress.getByName("localhost"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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
	void sendMouseClick(int x, int y) {
		try {
			MovementEvent me = new MovementEvent(GameEventNumber.MOVE_POS,
					client.getGame().getPlayer1().getId());
			me.setNewXPos(x);
			me.setNewYPos(y);
			String msg = NetworkMessageSerializer.serialize(me);
			client.sendMessage(msg);
		} catch (GivenParametersDoNotFitToEventException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new servercircle to drawpanel at given position.
	 * 
	 * @param x
	 *            x-position of new circle.
	 * @param y
	 *            x-position of new circle.
	 */
	public void newCircle(int x, int y) {
		cdp.addServerCircle(x, y);
		repaint();
	}
}
