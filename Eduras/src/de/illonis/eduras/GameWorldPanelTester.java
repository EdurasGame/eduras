package de.illonis.eduras;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import de.illonis.eduras.gui.GameWorldPanel;

/**
 * A Gameworld panel tester. It creates a black frame with a yellow circle that
 * moves into cursor direction.
 * 
 * @author illonis
 * 
 */
public class GameWorldPanelTester {

	public GameWorldPanelTester() {
		Game g = new Game();
		GameObject go = new GameObject();
		go.setXPosition(60);
		go.setYPosition(70);
		g.getObjects().add(go);
		GameWorldPanel gwp = new GameWorldPanel();
		GameWorker gameWorker = new GameWorker(g, gwp);

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
}
