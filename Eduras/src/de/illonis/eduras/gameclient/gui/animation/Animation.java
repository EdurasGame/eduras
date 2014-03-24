package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTarget;

import de.illonis.eduras.math.Vector2df;

/**
 * A graphical animation on the game panel.
 * 
 * @author illonis
 * 
 */
public abstract class Animation implements TimingTarget {
	private String name;
	private long duration;
	private long repeatCount;
	private long startDelay;
	protected Vector2df position;
	private boolean running;

	Animation(String name, long duration, Vector2df position) {
		this(name, duration, position, 1);
	}

	Animation(String name, long duration, Vector2df position, long repeatCount) {
		this.name = name;
		this.duration = duration;
		this.repeatCount = repeatCount;
		this.position = position;
		startDelay = 0;
		running = false;
	}

	/**
	 * Sets the start delay for this animation.
	 * 
	 * @param delay
	 *            delay in milliseconds.
	 */
	protected void setStartDelay(long delay) {
		this.startDelay = delay;
	}

	final Animator getAnimator() {
		return new Animator.Builder().setDebugName(name)
				.setStartDelay(startDelay, TimeUnit.MILLISECONDS)
				.setRepeatCount(repeatCount)
				.setDuration(duration, TimeUnit.MILLISECONDS).addTarget(this)
				.build();
	}

	/**
	 * Draws the animation onto given graphics target if it is running.<br>
	 * To draw game-relative, add <b>cameraX</b> and <b>cameraY</b> to your
	 * drawing coordinates, otherwise you will paint HUD-relative.
	 * 
	 * @param g2d
	 *            target graphics
	 * @param cameraX
	 *            the x-offset of the camera.
	 * @param cameraY
	 *            the y-offset of the camera.
	 */
	public final void draw(Graphics2D g2d, int cameraX, int cameraY) {
		if (running)
			drawAnimation(g2d, cameraX, cameraY);
	}

	/**
	 * Draws this animation onto given graphics.
	 * 
	 * @param g2d
	 *            target graphics object.
	 */
	protected abstract void drawAnimation(Graphics2D g2d, int cameraX,
			int cameraY);

	@Override
	public void begin(Animator source) {
		running = true;
	}

	@Override
	public void end(Animator source) {
		running = false;
		AnimationFactory.remove(this);
	}

	@Override
	public void reverse(Animator source) {
	}

	@Override
	public void repeat(Animator source) {
	}

	/**
	 * Passes a list of parameters to this animation to customize it. This will
	 * be called before Animator is created.
	 * 
	 * @param params
	 *            a list of parameters.
	 */
	public abstract void addParams(Object[] params);
}
