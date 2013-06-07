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

public class LoginAnimation extends JPanel {
	private final Animator animator;
	private LinkedList<Circle> circles;
	private final static int ANIM_HEIGHT = 60;
	private final static double LIMIT = 0.01;
	private final static Random r = new Random();

	public LoginAnimation() {
		super();

		animator = new Animator.Builder().setDebugName("LoginAnimation")
				.setRepeatCount(Animator.INFINITE)
				.setDuration(1500, TimeUnit.MILLISECONDS)
				.setRepeatBehavior(RepeatBehavior.LOOP).build();

	}

	public void start() {

		circles = new LinkedList<Circle>();
		for (int x = 0; x < 70; x++) {
			Circle c = new Circle();
			circles.add(c);
			animator.addTarget(c);
		}
		animator.start();
	}

	public void stop() {
		animator.stop();
	}

	private class Circle implements TimingTarget {
		private int x;
		private int y;
		private int oldY;
		private double delay;
		public final static int CIRCLE_WIDTH = 10;
		public final static int CIRCLE_HEIGHT = 10;

		private float alpha;

		public Circle() {
			myRepeat();
			alpha = 1;
			y = oldY;
			this.delay = r.nextFloat();
		}

		private void myRepeat() {
			this.x = r.nextInt(getWidth());
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

		for (Circle c : circles) {
			g2d.setPaint(new Color(0, 0, 1, c.alpha));
			g2d.fillOval(c.x, c.y, Circle.CIRCLE_WIDTH, Circle.CIRCLE_HEIGHT);
		}

		g2d.dispose();
	}
}
