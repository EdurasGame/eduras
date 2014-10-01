package de.illonis.eduras.gameclient.datacache;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.ResourceLoader;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.shapecreator.FileCorruptException;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;
import de.illonis.eduras.shapes.data.ShapeParser;
import de.illonis.eduras.utils.Pair;

/**
 * Prefetches graphics and shapes and loads them into cache.<br>
 * This loader does not return any data because loaded values are written to
 * {@link ImageCache}.
 * 
 * @author illonis
 * 
 */
public final class GraphicsPreLoader {

	private final static Logger L = EduLog.getLoggerFor(GraphicsPreLoader.class
			.getName());

	/**
	 * Backdoor for server to load only shapes. Loads shapes synchonous.
	 */
	public static void preLoadShapes() {
		loadShapes();
	}

	public static void loadTextures() {
		HashMap<TextureKey, String> textureInfo = CacheInfo.getAllTextures();
		Iterator<Map.Entry<TextureKey, String>> it = textureInfo.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<TextureKey, String> pair = it.next();
			try {
				Image image = ImageFiler.load(pair.getValue());
				ImageCache.addTexture(pair.getKey(), image);
			} catch (SlickException e) {
				L.log(Level.SEVERE,
						"Texture file not found: " + pair.getValue(), e);
			}
		}

	}

	public static void loadFonts() {
		Iterator<Map.Entry<FontKey, Pair<String, Integer>>> it = CacheInfo
				.getAllFonts().entrySet().iterator();
		float scale = GameRenderer.getRenderScale();
		while (it.hasNext()) {
			Map.Entry<FontKey, Pair<String, Integer>> pair = it.next();
			try {
				InputStream inputStream = ResourceLoader
						.getResourceAsStream("res/fonts/"
								+ pair.getValue().getFirst());
				Font awtFont2 = Font
						.createFont(Font.TRUETYPE_FONT, inputStream);
				awtFont2 = awtFont2.deriveFont(pair.getValue().getSecond()
						* scale); // set font size
				TrueTypeFont font2 = new TrueTypeFont(awtFont2, true);
				FontCache.addFont(pair.getKey(), font2);

			} catch (Exception e) {
				e.printStackTrace();
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
				Image image = ImageFiler.loadScaled(pair.getValue());
				ImageCache.addInventoryIcon(pair.getKey(), image);
			} catch (SlickException | IOException e) {
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
				Image image = ImageFiler.loadScaled(pair.getValue());
				ImageCache.addGuiImage(pair.getKey(), image);
			} catch (SlickException | IOException e) {
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
				Vector2f[] verts = ShapeParser.readShape(u);
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
				Image image = ImageFiler.loadScaled(pair.getValue());
				ImageCache.addImage(pair.getKey(), image);
			} catch (SlickException | IOException e) {
				L.log(Level.SEVERE, "Imagefile not found: " + pair.getValue(),
						e);
			}
		}
	}
}
