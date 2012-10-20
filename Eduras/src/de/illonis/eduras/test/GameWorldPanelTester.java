package de.illonis.eduras.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import de.illonis.eduras.Game;

/**
 * A Gameworld panel tester. It creates a black frame with a yellow circle that
 * moves into cursor direction.
 * 
 * @author illonis
 * 
 */
public class GameWorldPanelTester implements MouseListener {

	private YellowCircle yc;

	public GameWorldPanelTester() {
		Game g = new Game();
		yc = new YellowCircle(g);
		yc.setXPosition(60);
		yc.setYPosition(70);
		g.getObjects().add(yc);
		GameWorldPanel gwp = new GameWorldPanel();
		GameWorker gameWorker = new GameWorker(g, gwp);
		InfoText it = new InfoText(g, yc);
		it.setXPosition(10);
		it.setYPosition(30);
		g.getObjects().add(it);

		gwp.addMouseListener(this);

		JFrame testFrame = new JFrame("graphic-tester");
		testFrame.getContentPane().setLayout(new BorderLayout());
		testFrame.getContentPane().add(gwp, BorderLayout.CENTER);

		testFrame.setSize(new Dimension(500, 500));
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setLocationRelativeTo(null);
		Thread t = new Thread(gameWorker);
		testFrame.setVisible(true);
		t.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameWorldPanelTester();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			yc.setSpeed(yc.getSpeed() + 1);
		} else
			yc.setSpeed(yc.getSpeed() - 1);
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
}
