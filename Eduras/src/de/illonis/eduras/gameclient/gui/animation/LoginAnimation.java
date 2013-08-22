package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.core.animation.timing.TimingTarget;

/**
 * A very beautiful animation for the login panel.
 * 
 * @author illonis
 * 
 */
public class LoginAnimation extends JPanel {

	private static final long serialVersionUID = 1L;
	private final Animator animator;
	private LinkedList<Circle> circles;
	private final static int ANIM_HEIGHT = 60;
	private final static double LIMIT = 0.01;
	private final static Random r = new Random();

	/**
	 * Creates a new animation panel.
	 */
	public LoginAnimation() {
		super();

		animator = new Animator.Builder().setDebugName("LoginAnimation")
				.setRepeatCount(Animator.INFINITE)
				.setDuration(1000, TimeUnit.MILLISECONDS)
				.setRepeatBehavior(RepeatBehavior.LOOP).build();

	}

	/**
	 * Starts animation.
	 */
	public void start() {

		circles = new LinkedList<Circle>();
		for (int x = 0; x < 2000; x++) {
			Circle c = new Circle();
			circles.add(c);
			animator.addTarget(c);
		}
		animator.start();
	}

	/**
	 * Stops animation.
	 */
	public void stop() {
		animator.stop();
	}

	private class Circle implements TimingTarget {
		private int x;
		private int y;
		private int oldY;
		private double delay;
		private double addition;
		public final static int CIRCLE_WIDTH = 10;
		public final static int CIRCLE_HEIGHT = 10;

		private float alpha;

		public Circle() {
			myRepeat();
			alpha = 1;
			y = oldY;
			addition = 0;
			if (r.nextFloat() < 0.2)
				addition = r.nextFloat() * 20;

			this.delay = r.nextFloat();
		}

		private void myRepeat() {
			this.x = r.nextInt(getWidth() + 20) - 10;
			oldY = getHeight() + 10;
		}

		@Override
		public void begin(Animator source) {
		}

		@Override
		public void end(Animator source) {
		}

		@Override
		public void repeat(Animator source) {
		}

		@Override
		public void reverse(Animator source) {
		}

		@Override
		public void timingEvent(Animator source, double fraction) {
			// this calculates position of delayed circles because they need to
			// emulate an older step.
			double cfraction = fraction - delay;
			if (cfraction < 0)
				cfraction = 1 + cfraction;
			if (cfraction < LIMIT)
				myRepeat();

			y = oldY - (int) Math.round(ANIM_HEIGHT * cfraction);

			alpha = 1 - (float) ((double) (oldY - y) / ANIM_HEIGHT);
			repaint();

		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < circles.size(); i++) {
			Circle c = circles.get(i);
			g2d.setPaint(new Color(.67f, .003f, .0015f, c.alpha));
			g2d.fillOval(c.x, c.y - (int) c.addition, Circle.CIRCLE_WIDTH,
					Circle.CIRCLE_HEIGHT);
		}
		paintComponents(g2d);
		g2d.dispose();
	}
}
