package de.illonis.eduras.gameclient.gui;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * A storage for sounds.
 * 
 * @author illonis
 * 
 */
public class SoundMachine {

	/**
	 * Sound types.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum SoundType {
		CLICK;
	}

	/**
	 * Loads all sounds.
	 * 
	 * @throws SlickException
	 */
	public static void init() throws SlickException {
		sounds.put(SoundType.CLICK, new Sound("/res/audio/sound/click.ogg"));
	}

	private static final HashMap<SoundType, Sound> sounds;

	static {
		sounds = new HashMap<SoundType, Sound>();
	}

	/**
	 * Returns a sound for given type.
	 * 
	 * @param sound
	 *            the sound type.
	 * @return the sound of given type.
	 */
	public static Sound getSound(SoundType sound) {
		return sounds.get(sound);
	}

}
