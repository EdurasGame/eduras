package de.illonis.eduras.gameclient.datacache;

import java.util.HashMap;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;
import de.illonis.eduras.utils.Pair;

/**
 * Contains lists of cachable files and their location.<br>
 * Used to build the {@link ImageCache}.
 * 
 * @author illonis
 * 
 */
public final class CacheInfo {

	/**
	 * Keys for identifying cached images.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum ImageKey {
		STATISTICS_BG,
		RESOURCE_ICON,
		RESOURCE_ICON_SMALL,
		ACTION_HEAL,
		ACTION_RESURRECT,
		ACTION_RESURRECT_PLAYER,
		ACTION_ABORT,
		ACTION_SPAWN_OBSERVER,
		ACTION_SPELL_SCOUT,
		ACTION_SPAWN_ITEMS,
		ITEM_DUMMY,
		ITEM_SIMPLEWEAPON,
		ITEM_SNIPERWEAPON,
		ITEM_SPLASHWEAPON,
		ITEM_SWORDWEAPON,
		ITEM_ROCKETLAUNCHER,
		ITEM_MINELAUNCHER,
		ITEM_ASSAULTRIFLE,
		ACTION_SPELL_SPEED,
		ACTION_SPELL_INVISIBILITY,
		STRATEGY_MODE_ICON,
		SKILL_BLINK,
		ACTION_SPELL_BLINK,
		STRATEGY_BAR, ;

		public static ImageKey typeToImageKey(ObjectType type) {
			switch (type) {
			case ITEM_WEAPON_SIMPLE:
				return ImageKey.ITEM_SIMPLEWEAPON;
			case ITEM_WEAPON_SNIPER:
				return ImageKey.ITEM_SNIPERWEAPON;
			case ITEM_WEAPON_SPLASH:
				return ImageKey.ITEM_SPLASHWEAPON;
			case ITEM_WEAPON_SWORD:
				return ImageKey.ITEM_SWORDWEAPON;
			case ROCKETLAUNCHER:
				return ImageKey.ITEM_ROCKETLAUNCHER;
			case MINELAUNCHER:
				return ImageKey.ITEM_MINELAUNCHER;
			case ASSAULTRIFLE:
				return ImageKey.ITEM_ASSAULTRIFLE;
			default:
				return ImageKey.ITEM_DUMMY;
			}
		}
	}

	/**
	 * Absolute base url to resolve path for Slick.
	 */
	public final static String BASE_URL = "de/illonis/eduras/images/";

	private final static HashMap<ShapeType, String> shapes;
	private final static HashMap<ObjectType, String> objectImages;
	private final static HashMap<ImageKey, String> guiImages;
	private final static HashMap<ImageKey, String> imageIcons;
	private final static HashMap<ObjectType, String> inventoryIcons;
	private final static HashMap<FontKey, Pair<String, Integer>> fonts;
	private final static HashMap<TextureKey, String> textures;

	static {
		textures = new HashMap<TextureKey, String>();
		for (TextureKey texture : TextureKey.values()) {
			if (texture != TextureKey.NONE)
				textures.put(texture, "textures/" + texture.getFile());
		}
		fonts = new HashMap<FontKey, Pair<String, Integer>>();
		fonts.put(FontKey.DEFAULT_FONT, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 16));
		fonts.put(FontKey.SMALL_FONT, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 10));
		fonts.put(FontKey.BIG_FONT, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 20));
		fonts.put(FontKey.HUGE_FONT, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 24));
		fonts.put(FontKey.CHAT_FONT, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 14));
		fonts.put(FontKey.TOOLTIP_FONT, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 14));
		fonts.put(FontKey.NAMEPLATE, new Pair<String, Integer>(
				"LinLibertine_R.ttf", 10));
		shapes = new HashMap<ShapeType, String>();
		shapes.put(ShapeType.BIRD, "bird.esh");
		shapes.put(ShapeType.ROCKET, "rocket.esh");
		shapes.put(ShapeType.STAR, "mine.esh");
		shapes.put(ShapeType.WEAPON_SNIPER, "sniper.esh");
		shapes.put(ShapeType.WEAPON_1, "weapon1.esh");
		shapes.put(ShapeType.SWORD, "sword.esh");

		objectImages = new HashMap<ObjectType, String>();
		inventoryIcons = new HashMap<ObjectType, String>();

		inventoryIcons.put(ObjectType.ITEM_WEAPON_SIMPLE,
				"gui/icons/icon-weapon1.png");
		inventoryIcons.put(ObjectType.ITEM_WEAPON_SNIPER,
				"gui/icons/icon-weapon-sniper.png");
		inventoryIcons.put(ObjectType.ITEM_WEAPON_SPLASH,
				"gui/icons/icon-weapon-splash.png");
		inventoryIcons.put(ObjectType.ITEM_WEAPON_SWORD,
				"gui/icons/icon-sword.png");
		inventoryIcons.put(ObjectType.ROCKETLAUNCHER,
				"gui/icons/icon-rocketlauncher.png");
		inventoryIcons.put(ObjectType.ROCKET_MISSILE,
				"gui/icons/icon-rocketlauncher.png");
		inventoryIcons.put(ObjectType.MINELAUNCHER, "gui/icons/icon-mine.png");
		inventoryIcons.put(ObjectType.ASSAULTRIFLE,
				"gui/icons/icon-assaultrifle.png");

		objectImages.put(ObjectType.BIGBLOCK, "gameobjects/bigblock.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SNIPER,
				"gameobjects/sniper.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SIMPLE,
				"gameobjects/icon-weapon1.png");
		objectImages.put(ObjectType.ITEM_WEAPON_SWORD, "gameobjects/sword.png");
		objectImages.put(ObjectType.HEALING_POTION,
				"gameobjects/healthpotion.png");
		objectImages.put(ObjectType.INVISIBILITY_POWERUP,
				"gameobjects/invisibilitypowerup.png");
		objectImages.put(ObjectType.SPEED_POWERUP,
				"gameobjects/speedpowerup.png");
		guiImages = new HashMap<ImageKey, String>();
		guiImages.put(ImageKey.STATISTICS_BG, "gui/artwork/statwindow.png");
		guiImages.put(ImageKey.RESOURCE_ICON, "gui/icons/resource.png");
		guiImages.put(ImageKey.RESOURCE_ICON_SMALL,
				"gui/icons/resource_small.png");
		guiImages.put(ImageKey.STRATEGY_MODE_ICON,
				"gui/artwork/strategymode.png");
		guiImages.put(ImageKey.STRATEGY_BAR, "gui/artwork/strategybar.png");
		guiImages.put(ImageKey.ACTION_HEAL, "gui/icons/icon-heal.png");
		guiImages
				.put(ImageKey.ACTION_RESURRECT, "gui/icons/icon-resurrect.png");
		guiImages.put(ImageKey.ACTION_RESURRECT_PLAYER,
				"gui/icons/icon-resurrect.png");
		guiImages.put(ImageKey.ACTION_ABORT, "gui/icons/icon-abort.png");
		guiImages.put(ImageKey.ACTION_SPAWN_OBSERVER,
				"gui/icons/icon-spawn-observer.png");
		guiImages.put(ImageKey.ACTION_SPELL_SCOUT,
				"gui/icons/icon-spell-scout.png");
		guiImages.put(ImageKey.ACTION_SPELL_SPEED,
				"gui/icons/icon-speedspell.png");
		guiImages.put(ImageKey.ACTION_SPELL_INVISIBILITY,
				"gui/icons/icon-invis-spell.png");
		guiImages.put(ImageKey.ACTION_SPELL_BLINK,
				"gui/icons/icon-blink-spell.png");
		guiImages.put(ImageKey.SKILL_BLINK, "gui/icons/icon-blink.png");

		// items
		guiImages.put(ImageKey.ITEM_DUMMY, "gui/icons/icon-resurrect.png");
		guiImages.put(ImageKey.ACTION_SPAWN_ITEMS,
				"gui/icons/icon_spawn_item.png");
		guiImages.put(ImageKey.ITEM_ASSAULTRIFLE,
				inventoryIcons.get(ObjectType.ASSAULTRIFLE));
		guiImages.put(ImageKey.ITEM_MINELAUNCHER,
				inventoryIcons.get(ObjectType.MINELAUNCHER));
		guiImages.put(ImageKey.ITEM_ROCKETLAUNCHER,
				inventoryIcons.get(ObjectType.ROCKETLAUNCHER));
		guiImages.put(ImageKey.ITEM_SIMPLEWEAPON,
				inventoryIcons.get(ObjectType.ITEM_WEAPON_SIMPLE));
		guiImages.put(ImageKey.ITEM_SNIPERWEAPON,
				inventoryIcons.get(ObjectType.ITEM_WEAPON_SNIPER));
		guiImages.put(ImageKey.ITEM_SPLASHWEAPON,
				inventoryIcons.get(ObjectType.ITEM_WEAPON_SPLASH));
		guiImages.put(ImageKey.ITEM_SWORDWEAPON,
				inventoryIcons.get(ObjectType.ITEM_WEAPON_SWORD));

		imageIcons = new HashMap<ImageKey, String>();
	}

	/**
	 * Retrieves the filename that contains shapedata for given shape type.
	 * 
	 * @param type
	 *            the type of the shape.
	 * @return the filename.
	 */
	public static String getShapeFileName(ShapeType type) {
		return shapes.get(type);
	}

	/**
	 * Retrieves the filename that contains image data for given object type.
	 * 
	 * @param type
	 *            type of the object.
	 * @return the file name.
	 */
	public static String getObjectImageFile(ObjectType type) {
		return objectImages.get(type);
	}

	/**
	 * Retrieves the filename for an inventory icon for given object type.
	 * 
	 * @param type
	 *            type of the object.
	 * @return the file name.
	 */
	public static String getInventoryIconFile(ObjectType type) {
		return inventoryIcons.get(type);
	}

	/**
	 * Retrieves the filename of an imageicon with given key.
	 * 
	 * @param key
	 *            key for that image
	 * @return the file name.
	 */
	public static String getIconFile(ImageKey key) {
		return imageIcons.get(key);
	}

	/**
	 * Retrieves the filename of a gui image with given key.
	 * 
	 * @param key
	 *            key for that image
	 * @return the file name.
	 */
	public static String getGuiImageFile(ImageKey key) {
		return guiImages.get(key);
	}

	static HashMap<ShapeType, String> getAllShapes() {
		return new HashMap<ShapeType, String>(shapes);
	}

	static HashMap<ObjectType, String> getAllObjectImages() {
		return new HashMap<ObjectType, String>(objectImages);
	}

	static HashMap<ImageKey, String> getAllGuiImages() {
		return new HashMap<ImageKey, String>(guiImages);
	}

	static HashMap<FontKey, Pair<String, Integer>> getAllFonts() {
		return new HashMap<FontKey, Pair<String, Integer>>(fonts);
	}

	static HashMap<ImageKey, String> getAllImageIcons() {
		return new HashMap<ImageKey, String>(imageIcons);
	}

	static HashMap<ObjectType, String> getAllInventoryIcons() {
		return new HashMap<ObjectType, String>(inventoryIcons);
	}

	static HashMap<TextureKey, String> getAllTextures() {
		return new HashMap<TextureKey, String>(textures);
	}
}
