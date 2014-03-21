package de.illonis.eduras.gameclient.datacache;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapes.ObjectShape.ShapeType;
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

	private final static Logger L = EduLog.getLoggerFor(GraphicsPreLoader.class
			.getName());

	/**
	 * @param listener
	 *            the listener.
	 */
	public GraphicsPreLoader(AsyncLoadCompletedListener listener) {
		super(listener);
	}

	private GraphicsPreLoader() {
		this(new AsyncLoadCompletedListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
			}

			@Override
			public void onDataLoaded() {
			}
		});
	}

	/**
	 * Backdoor for server to load only shapes. Loads shapes synchonous.
	 */
	public static void preLoadShapes() {
		GraphicsPreLoader p = new GraphicsPreLoader();
		p.loadShapes();
	}

	private void loadIcons() {
		HashMap<ImageKey, String> shapeInfo = CacheInfo.getAllImageIcons();
		Iterator<Map.Entry<ImageKey, String>> it = shapeInfo.entrySet()
				.iterator();
		int n = shapeInfo.size();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<ImageKey, String> pair = it.next();
			try {
				ImageIcon image = ImageFiler.loadIcon(pair.getValue());
				ImageCache.addImageIcon(pair.getKey(), image);
			} catch (IllegalArgumentException e) {
				L.log(Level.SEVERE,
						"Guiimagefile not found: " + pair.getValue(), e);
			}
			i++;

			int progress = (int) Math.floor((double) i / n * 25) + 50;
			setProgress(progress);
		}
	}

	private void loadInventoryIcons() {
		HashMap<ObjectType, String> inventoryIconInfo = CacheInfo
				.getAllInventoryIcons();
		Iterator<Map.Entry<ObjectType, String>> it = inventoryIconInfo
				.entrySet().iterator();
		int n = inventoryIconInfo.size();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<ObjectType, String> pair = it.next();
			try {
				BufferedImage image = ImageFiler.load(pair.getValue());
				ImageCache.addInventoryIcon(pair.getKey(), image);
			} catch (IllegalArgumentException | IOException e) {
				L.log(Level.SEVERE,
						"Inventory icon not found: " + pair.getValue(), e);
			}
			i++;

			int progress = (int) Math.floor((double) i / n * 25) + 50;
			setProgress(progress);
		}
	}

	private void loadGuiGraphics() {
		HashMap<ImageKey, String> shapeInfo = CacheInfo.getAllGuiImages();
		Iterator<Map.Entry<ImageKey, String>> it = shapeInfo.entrySet()
				.iterator();
		int n = shapeInfo.size();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<ImageKey, String> pair = it.next();
			try {
				BufferedImage image = ImageFiler.load(pair.getValue());
				ImageCache.addGuiImage(pair.getKey(), image);
			} catch (IllegalArgumentException | IOException e) {
				L.log(Level.SEVERE,
						"Guiimagefile not found: " + pair.getValue(), e);
			}
			i++;

			int progress = (int) Math.floor((double) i / n * 25) + 75;
			setProgress(progress);
		}
	}

	private void loadShapes() {
		HashMap<ShapeType, String> shapeInfo = CacheInfo.getAllShapes();
		Iterator<Map.Entry<ShapeType, String>> it = shapeInfo.entrySet()
				.iterator();
		int n = shapeInfo.size();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<ShapeType, String> pair = it.next();
			URL u = ShapeParser.class.getResource(pair.getValue());
			try {
				Vector2D[] verts = ShapeParser.readShape(u);
				ImageCache.addShape(pair.getKey(), verts);
			} catch (FileCorruptException | IOException e) {
				L.log(Level.SEVERE, "Shapefile not found: " + pair.getValue(),
						e);
			}
			i++;
			// shapes represent the first half of loading, graphics the other
			int progress = (int) Math.floor((double) i / n * 25);
			setProgress(progress);
			// it.remove(); // avoids a ConcurrentModificationException
		}
	}

	private void loadGraphics() {
		HashMap<ObjectType, String> shapeInfo = CacheInfo.getAllObjectImages();
		Iterator<Map.Entry<ObjectType, String>> it = shapeInfo.entrySet()
				.iterator();
		int n = shapeInfo.size();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<ObjectType, String> pair = it.next();
			try {
				BufferedImage image = ImageFiler.load(pair.getValue());
				ImageCache.addImage(pair.getKey(), image);
			} catch (IllegalArgumentException | IOException e) {
				L.log(Level.SEVERE, "Imagefile not found: " + pair.getValue(),
						e);
			}
			i++;

			int progress = (int) Math.floor((double) i / n * 25) + 25;
			setProgress(progress);
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
