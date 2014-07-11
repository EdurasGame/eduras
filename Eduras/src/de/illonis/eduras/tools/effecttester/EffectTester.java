package de.illonis.eduras.tools.effecttester;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;

/**
 * A tool to test effects.
 * 
 * @author illonis
 * 
 */
public class EffectTester extends BasicGame {

	private final static Logger L = EduLog.getLoggerFor(EffectTester.class
			.getName());

	private ParticleSystem system;
	private ConfigurableEmitter emitter;
	private final EffectNumber[] effects;
	private int current;
	private boolean repeat;

	/**
	 * @param args
	 *            none.
	 * @throws SlickException
	 *             if start failed.
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new EffectTester());
		app.setDisplayMode(800, 600, false);
		app.setAlwaysRender(true);
		app.start();
	}

	private EffectTester() {
		super("Effect tester");
		effects = EffectNumber.values();
		current = 0;
		repeat = false;
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		g.drawString(
				getCurrent().name() + " | Particle count: "
						+ system.getParticleCount(), 10, 25);
		g.drawString("Press spacebar to reload from files.", 10, 40);

		g.drawString("auto-repeat: " + Boolean.toString(repeat), 600, 40);

		g.drawString(
				"Press right/left arrow key to go to next/previous effect", 10,
				55);
		if (emitter.length.isEnabled()) {
			g.setColor(Color.yellow);
			g.drawString(
					"This effect has limited lifetime ("
							+ emitter.length.getMin() + " to "
							+ emitter.length.getMax() + ").", 10, 80);

			g.drawString("Reload to repeat (or press r to toggle auto-repeat)",
					10, 95);
		}
		system.render();
	}

	@Override
	public void init(GameContainer container) {
		try {
			loadEffect(getCurrent());
		} catch (SlickException | IOException e) {
			L.log(Level.SEVERE, "Could not load effect " + getCurrent().name(),
					e);
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		system.update(delta);
		if (emitter.completed() && repeat) {
			emitter.replay();
		}
	}

	private void next() {
		current++;
		if (current >= effects.length)
			current = 0;
	}

	private void prev() {
		current--;
		if (current < 0)
			current = effects.length - 1;
	}

	private EffectNumber getCurrent() {
		return effects[current];
	}

	private void loadEffect(EffectNumber effect) throws SlickException,
			IOException {
		Image image = new Image(effect.getImage(), false);
		system = new ParticleSystem(image, 1500);
		File xmlFile = new File(effect.getConfiguration());
		emitter = ParticleIO.loadEmitter(xmlFile);
		emitter.setPosition(400, 300);
		system.addEmitter(emitter);
		system.setRemoveCompletedEmitters(false);
		system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
	}

	private void reload() {
		try {
			loadEffect(getCurrent());
		} catch (SlickException | IOException e) {
			L.log(Level.SEVERE, "Could not load effect " + getCurrent().name(),
					e);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_SPACE:
			reload();
			break;
		case Input.KEY_LEFT:
			prev();
			reload();
			break;
		case Input.KEY_RIGHT:
			next();
			reload();
			break;
		case Input.KEY_R:
			repeat = !repeat;
			break;
		case Input.KEY_ESCAPE:
			System.exit(0);
			break;
		default:
			break;
		}
	}
}
