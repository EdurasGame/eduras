package de.illonis.eduras.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import de.illonis.eduras.mapeditor.EditorGame;
import de.illonis.eduras.mapeditor.StatusListener;

/**
 * Window for map editor.
 * 
 * @author illonis
 * 
 */
public class EditorWindow extends JFrame implements StatusListener {

	private static final long serialVersionUID = 1L;
	/**
	 * Basic window title.
	 */
	public final static String BASE_TITLE = " - Eduras? Map Editor";

	private ObjectPlacingSelectionPanel objectListing;
	private CanvasGameContainer gameContainer;
	private ToolMenuBar quickMenu;
	private StatusBar statusBar;

	/**
	 * Creates the window.
	 */
	public EditorWindow() {
		super(BASE_TITLE);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new FrameListener());
	}

	private class FrameListener extends WindowAdapter {

		public void tryExit() {
			int result = JOptionPane
					.showConfirmDialog(
							EditorWindow.this,
							"Do you really want to exit?\nUnsaved changes will be lost.",
							"Exiting", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			tryExit();
		}
	}

	/**
	 * Setup the gui.
	 * 
	 * @param game
	 *            the editor pseudo-game.
	 * @throws SlickException
	 *             if initialization failed.
	 */
	public void initGui(EditorGame game) throws SlickException {
		JPanel content = (JPanel) getContentPane();
		content.setLayout(new BorderLayout());

		quickMenu = new ToolMenuBar(game.getPanelLogic());
		objectListing = new ObjectPlacingSelectionPanel(game.getPanelLogic(),
				quickMenu);
		quickMenu.setOther(objectListing);
		content.add(objectListing, BorderLayout.WEST);
		gameContainer = new CanvasGameContainer(game);
		gameContainer.setSize(800, 600);
		content.add(gameContainer, BorderLayout.CENTER);
		setJMenuBar(new EditorMenu());
		content.add(quickMenu, BorderLayout.NORTH);
		statusBar = new StatusBar();
		content.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Starts map rendering.
	 * 
	 * @throws SlickException
	 *             on error.
	 */
	public void startRendering() throws SlickException {
		gameContainer.start();
	}

	@Override
	public void setStatus(String text) {
		statusBar.setStatus(text);
	}
}
