package de.illonis.eduras.gameclient.audio;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.illonis.edulog.EduLog;

/**
 * A storage and player for sound effects.
 * 
 * @author illonis
 * 
 */
public class SoundMachine {

	private static final HashMap<SoundType, Sound> sounds = new HashMap<SoundType, Sound>();
	private final static String baseFolder = "/res/audio/sound/";
	private final static Logger L = EduLog.getLoggerFor(SoundMachine.class
			.getName());

	/**
	 * All available sound types.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum SoundType {
		CLICK("click.ogg"), ERROR("error.ogg");

		private final String file;

		private SoundType(String filename) {
			this.file = filename;
		}

		public String getFile() {
			return file;
		}
	}

	/**
	 * Loads all sounds.
	 * 
	 * @throws SlickException
	 *             if there was an error loading sound files.
	 */
	public static void init() throws SlickException {
		if (!sounds.isEmpty())
			throw new IllegalStateException("Sounds were already initialized.");
		for (SoundType sound : SoundType.values()) {
			sounds.put(sound, new Sound(baseFolder + sound.getFile()));
		}
	}

	/**
	 * Plays the sound with standard volume.
	 * 
	 * @param sound
	 *            the sound to play.
	 * @param pitch
	 *            The pitch to play the sound effect at.
	 */
	public static void play(SoundType sound, float pitch) {
		play(sound, pitch, 1f);
	}

	/**
	 * Plays the sound at default volume and pitch
	 * 
	 * @param sound
	 *            the sound to play.
	 */
	public static void play(SoundType sound) {
		play(sound, 1f, 1f);
	}

	/**
	 * Plays the sound with given volume and pitch.
	 * 
	 * @param sound
	 *            the sound to play.
	 * @param pitch
	 *            The pitch to play the sound effect at.
	 * @param volume
	 *            The volumen to play the sound effect at
	 */
	public static void play(SoundType sound, float pitch, float volume) {
		Sound s = sounds.get(sound);
		if (s != null) {
			s.play(pitch, volume);
		} else {
			L.log(Level.WARNING, "Sound " + sound + " not found.");
		}
	}

	/**
	 * This method is deprecated.<br>
	 * Use {@link #play(SoundType)}, {@link #play(SoundType, float)} or
	 * {@link #play(SoundType, float, float)} instead.
	 * 
	 * @param sound
	 *            the sound type.
	 * @return the sound of given type.
	 */
	@Deprecated
	public static Sound getSound(SoundType sound) {
		return sounds.get(sound);
	}

}
