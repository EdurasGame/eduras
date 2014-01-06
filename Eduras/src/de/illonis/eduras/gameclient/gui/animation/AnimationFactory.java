package de.illonis.eduras.gameclient.gui.animation;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.math.Vector2D;

/**
 * Manages animation framework and generates animations.
 * 
 * @author illonis
 * 
 */
public final class AnimationFactory {

	private final static Logger L = EduLog.getLoggerFor(AnimationFactory.class
			.getName());

	private static ClientData data;

	/**
	 * Animation identifiers.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum AnimationNumber {
		DEMO;
	}

	private AnimationFactory() {
	}

	private static TimingSource timingSource;

	/**
	 * Initializes timing framework.
	 * 
	 * @param clientData
	 *            the client data. Active animations will be stored here.
	 */
	public static void init(ClientData clientData) {
		data = clientData;
		timingSource = new SwingTimerTimingSource();
		Animator.setDefaultTimingSource(timingSource);
		timingSource.init();
		System.out.println("init animation factory");

	}

	/**
	 * Cleans up timing framework.
	 */
	public static void dispose() {
		data.clearAnimations();
		if (timingSource != null)
			timingSource.dispose();
	}

	/**
	 * Runs an animation.
	 * 
	 * @param animationNumber
	 *            the animation to run.
	 * @param mapPosition
	 *            the position in the world to run that animation at.
	 */
	public static void runAt(AnimationNumber animationNumber,
			Vector2D mapPosition) {
		// do not run animations on server side (it won't work either)
		if (data == null)
			return;
		try {
			Animation animation = createAnimation(animationNumber, mapPosition);
			data.addAnimation(animation);
			animation.getAnimator().start();
		} catch (AnimationNotFoundException e) {
			L.log(Level.WARNING, "Could not found animation " + animationNumber
					+ ".", e);
		}
	}

	private static Animation createAnimation(AnimationNumber number,
			Vector2D position) throws AnimationNotFoundException {
		switch (number) {
		case DEMO:
			return new DemoAnimation(position);
		default:
			throw new AnimationNotFoundException(number);
		}
	}

	static void remove(Animation animation) {
		data.removeAnimation(animation);
	}

}
