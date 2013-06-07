package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.core.animation.timing.TimingTarget;

public class LoginAnimation extends JPanel implements TimingTarget {
	private final Animator animator;
	private int x;
	private int y;
	private int max;
	private int oldX;
	private int width = 30;
	private int height = 30;

	public LoginAnimation() {
		super();
		x = 0;
		y = 80;
		oldX = 0;
		max = 300;
		animator = new Animator.Builder().setDebugName("LoginAnimation")
				.setStartDelay(1, TimeUnit.SECONDS)
				.setRepeatCount(Animator.INFINITE)
				.setDuration(5, TimeUnit.SECONDS)
				.setRepeatBehavior(RepeatBehavior.LOOP).addTarget(this).build();
	}

	public void start() {
		animator.start();
	}

	public void stop() {
		animator.stop();
	}

	public void begin(Animator source) {
		System.out.println("start");
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

		oldX = x;

		x = (int) Math.round((double) max * fraction);
		repaint();
		System.out.println(fraction);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLUE);
		g2d.fillRect(x, y, width, height);

		System.out.println("drawing at " + x);
		g2d.dispose();
	}
}
