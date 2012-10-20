package de.illonis.eduras;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * A panel where circles are drawn by click and send to server.
 * 
 * @author illonis
 * 
 */
public class CircleDrawPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	private ClientFrame clientFrame;

	private ArrayList<Ellipse2D.Double> circles, receivedCircles;

	/**
	 * Creates a circlepanel that notifies given clientFrame on click.
	 * 
	 * @param clientFrame
	 *            ClientFrame to notify.
	 */
	public CircleDrawPanel(ClientFrame clientFrame) {
		super();
		this.clientFrame = clientFrame;
		circles = new ArrayList<Ellipse2D.Double>();
		receivedCircles = new ArrayList<Ellipse2D.Double>();
		addMouseListener(this);
	}

	/**
	 * Adds a circle to server-received circle-list
	 * 
	 * @param d
	 *            x-Position of circle
	 * @param e
	 *            y-Position of circle
	 */
	public void addServerCircle(double d, double e) {
		receivedCircles.add(new Ellipse2D.Double(d - 5, e - 5, 10, 10));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("Clicked at " + x + ", " + y);
		circles.add(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
		clientFrame.sendMouseClick(x, y);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLUE);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.drawString("Click to draw blue circles and send them.", 5, 20);
		g2d.setColor(Color.RED);
		g2d.drawString("Red circles are received from server.", 5, 40);
		g2d.setColor(Color.BLUE);
		for (Ellipse2D.Double e : circles) {
			g2d.fill(e);
		}
		g2d.setColor(Color.RED);
		for (Ellipse2D.Double e : receivedCircles) {
			g2d.fill(e);
		}
		g2d.dispose();
	}
}
