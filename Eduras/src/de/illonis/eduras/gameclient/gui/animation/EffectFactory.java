package de.illonis.eduras.gameclient.gui.animation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import de.illonis.edulog.EduLog;

/**
 * Creates effects.
 * 
 * @author illonis
 * 
 */
public class EffectFactory {
	private final static Logger L = EduLog.getLoggerFor(EffectFactory.class
			.getName());

	private final static HashMap<EffectNumber, ParticleSystem> systems = new HashMap<EffectNumber, ParticleSystem>();

	/**
	 * Identifier for an effect.<br>
	 * Each effect will be initializied at gamestart and is then available to
	 * {@link ClientEffectHandler}.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum EffectNumber {
		TEST("explosion.png", "explosion.xml"), ROCKET("explosion.png",
				"rocket_explosion.xml"), ROCKET_MISSILE("explosion.png",
				"rocket_missile.xml");

		private final String image;
		private final String configuration;

		/**
		 * Creates a new type of effect.
		 * 
		 * @param imagePath
		 *            the path to the particle image. Relative to
		 *            <code>/res/images/particles</code>
		 * @param configuration
		 *            the path to the configuration file for effect. Relative to
		 *            <code>/res/particles</code>
		 */
		private EffectNumber(String imagePath, String configuration) {
			this.image = imagePath;
			this.configuration = configuration;
		}

		/**
		 * @return the path to config file.
		 */
		public String getConfiguration() {
			// leading slash is required here as this is loaded in another way
			return "/res/particles/" + configuration;
		}

		/**
		 * @return the image path.
		 */
		public String getImage() {
			return "res/images/particles/" + image;
		}
	}

	/**
	 * Loads effects and sets them up.
	 */
	public static void init() {
		for (EffectNumber effect : EffectNumber.values()) {
			try {
				Image image = new Image(effect.getImage(), false);
				ParticleSystem system = new ParticleSystem(image, 1500);
				InputStream xmlFile = EffectFactory.class
						.getResourceAsStream(effect.getConfiguration());
				ConfigurableEmitter emitter = ParticleIO.loadEmitter(xmlFile);
				emitter.setEnabled(false);
				system.setPosition(0, 0);
				system.setRemoveCompletedEmitters(true);
				system.addEmitter(emitter);
				system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
				systems.put(effect, system);
			} catch (Exception e) {
				L.log(Level.SEVERE, "Could not load particle data for effect "
						+ effect.name(), e);
			}

		}
	}

	/**
	 * Creates an effect at given position.
	 * 
	 * @param effect
	 *            type of effect.
	 * @param position
	 *            the topleft position to spawn this effect at.
	 */
	static ConfigurableEmitter createEffectAt(EffectNumber effect,
			Vector2f position) {
		return createEffectAt(effect, position, 0f);
	}

	/**
	 * Creates an effect at given position.
	 * 
	 * @param effect
	 *            type of effect.
	 * @param position
	 *            the topleft position to spawn this effect at.
	 * @param angle
	 *            the rotation offset in degree.
	 */
	static ConfigurableEmitter createEffectAt(EffectNumber effect,
			Vector2f position, float angle) {
		ParticleSystem source = systems.get(effect);
		if (source == null) {
			return null;
		}
		synchronized (source) {
			ConfigurableEmitter emitter = (ConfigurableEmitter) source
					.getEmitter(0);
			ConfigurableEmitter newEmitter = emitter.duplicate();
			if (angle != 0f) {
				newEmitter.angularOffset.setValue(newEmitter.angularOffset
						.getValue(0) + angle);
			}
			newEmitter.setPosition(position.x, position.y, true);
			newEmitter.setEnabled(true);
			source.addEmitter(newEmitter);
			return newEmitter;
		}
	}

	/**
	 * @return a list of all effect systems to be rendered.
	 */
	public static HashMap<EffectNumber, ParticleSystem> getSystems() {
		return systems;
	}
}
