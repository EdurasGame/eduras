package de.illonis.eduras.gameclient.datacache;

/**
 * Contains texture information.
 * 
 * @author illonis
 * 
 */
public class TextureInfo {

	/**
	 * Key for identifying textures.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum TextureKey {
		DUST_GRASS("grass2.png"),
		GRASS("grass.png"),
		REDROCK("testtexture.png"),
		BASE("base.png"),
		BASE_RED("base_red.png"),
		BASE_BLUE("base_blue.png"),
		PORTAL("portal.png"),
		BRICK("brick.png"),
		ROCK("rock.png"),
		ROOF("roof.png"),
		WATER("water.png"),
		NONE("");

		private final String file;

		TextureKey(String file) {
			this.file = file;
		}

		public String getFile() {
			return file;
		}
	}
}