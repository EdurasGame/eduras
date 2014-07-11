package de.illonis.eduras.gameclient.gui.animation;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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

	public enum EffectNumber {
		TEST("res/images/particles/explosion.png",
				"res/particles/explosion.xml"), ROCKET(
				"res/images/particles/explosion.png",
				"res/particles/rocket_explosion.xml");

		private final String image;
		private final String configuration;

		private EffectNumber(String imagePath, String configuration) {
			this.image = imagePath;
			this.configuration = configuration;
		}

		public String getConfiguration() {
			return configuration;
		}

		public String getImage() {
			return image;
		}
	}

	public static void init() {
		for (EffectNumber effect : EffectNumber.values()) {
			try {
				Image image = new Image(effect.getImage(), false);
				ParticleSystem system = new ParticleSystem(image, 1500);
				File xmlFile = new File(effect.getConfiguration());
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

	public static void createEffectAt(EffectNumber effect, Vector2f position)
			throws SlickException {
		ParticleSystem source = systems.get(effect);
		if (source == null) {
			return;
		}
		synchronized (source) {

			ConfigurableEmitter emitter = (ConfigurableEmitter) source
					.getEmitter(0);
			ConfigurableEmitter newEmitter = emitter.duplicate();
			newEmitter.setPosition(position.x, position.y);
			newEmitter.setEnabled(true);
			source.addEmitter(newEmitter);
		}
	}

	public static HashMap<EffectNumber, ParticleSystem> getSystems() {
		return systems;
	}
}
