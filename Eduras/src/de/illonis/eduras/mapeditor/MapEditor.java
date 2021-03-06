package de.illonis.eduras.mapeditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import org.newdawn.slick.SlickException;

import de.illonis.eduras.SysOutCatcher;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.mapeditor.validate.MapValidator;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

/**
 * A map editor for Eduras?-maps that supports easy placing of objects on the
 * map.
 * 
 * @author illonis
 * 
 */
public class MapEditor {

	/**
	 * Editor version.
	 */
	public final static int VERSION = 6;

	private EditorGame game;
	private EditorWindow window;

	/**
	 * @param args
	 *            <i>run with arguments if starting from eclipse</i>
	 */
	public static void main(String[] args) {
		boolean debug = args.length > 0;
		if (!debug) {
			System.setProperty("org.lwjgl.librarypath", ResourceManager
					.resourceToPath(ResourceType.NATIVE_LIBRARY, "").toString());
		} else {
			S.resource_folder = args[0];
			System.out.println("Setting resource folder to "
					+ S.resource_folder);
		}
		MapValidator.init();

		try {
			MapEditor editor = new MapEditor();
			editor.startUpdateCheck();
			editor.showWindow();
		} catch (UnsatisfiedLinkError e) {
			JOptionPane
					.showMessageDialog(
							null,
							"<html>Please place mapeditor executable in<br>&lt;eduras&gt;/game folder and start it again!");
			return;
		} catch (SlickException e) {
			JOptionPane.showMessageDialog(null, "Could not init slick.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void startUpdateCheck() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Thread.yield();
				URL obj;
				try {
					obj = new URL(
							"http://www.illonis.de/edurasmapeditorversion.txt");

					HttpURLConnection con = (HttpURLConnection) obj
							.openConnection();
					// optional default is GET
					con.setRequestMethod("GET");
					BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					int foreignVersion = Integer.parseInt(response.toString());
					if (foreignVersion > VERSION) {
						JOptionPane
								.showMessageDialog(
										window,
										"Your MapEditor is outdated.\n"
												+ "Please download the latest version from project platform.",
										"Update available",
										JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException | NumberFormatException e) {
					e.printStackTrace();
					return;
				}
			}
		});
		t.start();
	}

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
		case BIGBLOCK:
		case BIGGERBLOCK:
		case BUILDING:
		case ITEM_WEAPON_SIMPLE:
		case ITEM_WEAPON_SNIPER:
		case ITEM_WEAPON_SPLASH:
		case ITEM_WEAPON_SWORD:
		case PORTAL:
		case HEALING_POTION:
		case SPEED_POWERUP:
		case INVISIBILITY_POWERUP:
		case ROCKETLAUNCHER:
		case MINELAUNCHER:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Updates window title with new map name.
	 */
	public void onMapChanged() {
		window.setTitle(EditorWindow.BASE_TITLE);
	}

	private void init() throws SlickException {
		window = new EditorWindow();
		game = new EditorGame(window);
		window.initGui(game);
		onMapChanged();
	}
}
