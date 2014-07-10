package de.illonis.eduras.gameclient.datacache;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;
import de.illonis.eduras.shapes.data.ShapeParser;

/**
 * Prefetches graphics and shapes and loads them into cache.<br>
 * This loader does not return any data because loaded values are written to
 * {@link ImageCache}.
 * 
 * @author illonis
 * 
 */
public final class GraphicsPreLoader extends AsyncLoader<Void> {

	private final static LinkedList<CacheReadyListener> listeners = new LinkedList<CacheReadyListener>();

	private final static Logger L = EduLog.getLoggerFor(GraphicsPreLoader.class
			.getName());

	/**
	 * @param listener
	 *            the listener.
	 */
	public GraphicsPreLoader(AsyncLoadCompletedListener listener) {
		super(listener);
	}

	public static void addCacheListener(CacheReadyListener listener) {
		listeners.add(listener);
	}

	/**
	 * Backdoor for server to load only shapes. Loads shapes synchonous.
	 */
	public static void preLoadShapes() {
		loadShapes();
	}

	public static void loadIcons() {
		HashMap<ImageKey, String> shapeInfo = CacheInfo.getAllImageIcons();
		Iterator<Map.Entry<ImageKey, String>> it = shapeInfo.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<ImageKey, String> pair = it.next();
			try {
				ImageIcon image = ImageFiler.loadIcon(pair.getValue());
				ImageCache.addImageIcon(pair.getKey(), image);
			} catch (IllegalArgumentException e) {
				L.log(Level.SEVERE,
						"Guiimagefile not found: " + pair.getValue(), e);
			}
		}
	}

	public static void loadInventoryIcons() {
		HashMap<ObjectType, String> inventoryIconInfo = CacheInfo
				.getAllInventoryIcons();
		Iterator<Map.Entry<ObjectType, String>> it = inventoryIconInfo
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<ObjectType, String> pair = it.next();
			try {
				Image image = ImageFiler.load(pair.getValue());
				ImageCache.addInventoryIcon(pair.getKey(), image);
			} catch (SlickException e) {
				L.log(Level.SEVERE,
						"Inventory icon not found: " + pair.getValue(), e);
			}
		}
	}

	public static void loadGuiGraphics() {
		HashMap<ImageKey, String> shapeInfo = CacheInfo.getAllGuiImages();
		Iterator<Map.Entry<ImageKey, String>> it = shapeInfo.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<ImageKey, String> pair = it.next();
			try {
				Image image = ImageFiler.load(pair.getValue());
				ImageCache.addGuiImage(pair.getKey(), image);
			} catch (SlickException e) {
				L.log(Level.SEVERE,
						"Guiimagefile not found: " + pair.getValue(), e);
			}
		}
	}

	public static void loadShapes() {
		HashMap<ShapeType, String> shapeInfo = CacheInfo.getAllShapes();
		Iterator<Map.Entry<ShapeType, String>> it = shapeInfo.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<ShapeType, String> pair = it.next();
			URL u = ShapeParser.class.getResource(pair.getValue());
			try {
				Vector2df[] verts = ShapeParser.readShape(u);
				ImageCache.addShape(pair.getKey(), verts);
			} catch (FileCorruptException | IOException e) {
				L.log(Level.SEVERE, "Shapefile not found: " + pair.getValue(),
						e);
			}
		}
	}

	public static void loadGraphics() {
		HashMap<ObjectType, String> shapeInfo = CacheInfo.getAllObjectImages();
		Iterator<Map.Entry<ObjectType, String>> it = shapeInfo.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<ObjectType, String> pair = it.next();
			try {
				Image image = ImageFiler.load(pair.getValue());
				ImageCache.addImage(pair.getKey(), image);
			} catch (SlickException e) {
				L.log(Level.SEVERE, "Imagefile not found: " + pair.getValue(),
						e);
			}
		}
	}

	@Override
	protected Void doInBackground() throws Exception {
		loadShapes();
		setProgress(25);
		loadGraphics();
		setProgress(50);
		loadIcons();
		loadInventoryIcons();
		setProgress(75);
		loadGuiGraphics();
		setProgress(100);
		return null;
	}

}
