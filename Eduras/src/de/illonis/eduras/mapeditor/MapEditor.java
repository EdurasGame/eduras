package de.illonis.eduras.mapeditor;

import javax.swing.JOptionPane;

import org.newdawn.slick.SlickException;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.mapeditor.gui.EditorWindow;

/**
 * A map editor for Eduras?-maps that supports easy placing of objects on the
 * map.
 * 
 * @author illonis
 * 
 */
public class MapEditor {

	public static void main(String[] args) {
		try {
			MapEditor editor = new MapEditor();
			editor.showWindow();
		} catch (SlickException e) {
			JOptionPane.showMessageDialog(null, "Could not init slick.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private EditorGame game;
	private EditorWindow window;

	private MapEditor() throws SlickException {
		init();
	}

	private void showWindow() throws SlickException {
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.startRendering();
	}

	/**
	 * Checks if a specific object type is supported and can be placed in map
	 * editor.
	 * 
	 * @param type
	 *            the type to test for.
	 * @return true if given object type is supported.
	 */
	public static boolean isSupported(ObjectType type) {
		switch (type) {
		case ASSAULTRIFLE:

			return true;
		default:
			return false;
		}
	}

	private void init() throws SlickException {
		window = new EditorWindow();
		game = new EditorGame(window);
		window.initGui(game);
	}
}
