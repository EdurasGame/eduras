package de.illonis.eduras.gameclient.audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

/**
 * A storage and player for sound effects.
 * 
 * @author illonis
 * 
 */
public class SoundMachine {

	private static final HashMap<SoundType, Sound> sounds = new HashMap<SoundType, Sound>();
	private final static Logger L = EduLog.getLoggerFor(SoundMachine.class
			.getName());
	private final static String localBaseFolder = "res/audio/sound/";

	/**
	 * All available sound types.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum SoundType {
		CLICK("click.ogg"),
		ERROR("error2.ogg"),
		AMMO_EMPTY("error1.ogg"),
		SHOOT("shoot.ogg"),
		HURT("hurt.ogg"),
		LOOT("item_loot.ogg"),
		BASE_CONQUERED("base_conquered.ogg"),
		BASE_LOST("base_lost.ogg"),
		SPELL_REZZ("rezz1.ogg"),
		SPELL_HEAL("spell8.ogg");

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
			try (InputStream in = ResourceManager.openResource(
					ResourceType.SOUND, sound.getFile())) {
				sounds.put(sound, new Sound(in, sound.getFile()));
				L.log(Level.INFO, "Loaded sound " + sound.getFile());
			} catch (IOException e) {
				L.log(Level.WARNING, "Could not load sound " + sound.getFile(),
						e);
			}
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

}
