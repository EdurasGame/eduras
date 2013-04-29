package de.illonis.eduras.gameclient.gui;

import java.awt.Color;

import javax.swing.JPanel;

import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * A panel that represents the gameworld. All world objects and user interface
 * are displayed here.
 * 
 * @author illonis
 * 
 */
public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameRenderer renderer;

	/**
	 * Creates a new gamePanel with black background.
	 */
	public GamePanel() {
		setFocusable(true);
		setBackground(Color.black);
	}

	/**
	 * Initializes game renderer. This sets up game renderer and returns
	 * correspondend tooltip handler.
	 * 
	 * @param camera
	 *            camera.
	 * @param gui
	 *            user interface.
	 * @param infos
	 *            game information.
	 */
	void initRenderer(GameCamera camera, UserInterface gui,
			InformationProvider infos) {
		renderer = new GameRenderer(camera, gui, infos);
		renderer.setTarget(this);
	}

	/**
	 * Stopps rendering process.
	 */
	void stopRendering() {
		renderer.stopRendering();
	}

	/**
	 * Starts rendering process.
	 */
	void startRendering() {
		renderer.startRendering();
	}

}
