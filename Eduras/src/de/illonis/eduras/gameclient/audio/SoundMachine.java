package de.illonis.eduras.gameclient.audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.settings.S;
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
		CLICK("click.ogg"), ERROR("error2.wav"), AMMO_EMPTY("error1.wav"), SHOOT(
				"shoot.ogg"), HURT("hurt.wav"), LOOT("item_loot.wav"), BASE_CONQUERED(
				"base_conquered.wav"), BASE_LOST("base_lost.wav"), SPELL_REZZ(
				"rezz1.wav"), SPELL_HEAL("spell8.wav");

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
			if (S.Client.localres) {
				sounds.put(sound, new Sound(localBaseFolder + sound.getFile()));
			} else {
				try (InputStream in = ResourceManager.openResource(
						ResourceType.SOUND, sound.getFile())) {
					sounds.put(sound, new Sound(in, sound.getFile()));
					L.log(Level.INFO, "Loaded sound " + sound.getFile());
				} catch (IOException e) {
					L.log(Level.WARNING,
							"Could not load sound " + sound.getFile(), e);
				}
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
