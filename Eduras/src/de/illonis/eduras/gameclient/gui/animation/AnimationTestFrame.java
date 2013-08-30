package de.illonis.eduras.gameclient.gui.animation;

import javax.swing.JFrame;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

public class AnimationTestFrame extends JFrame {
	LoginAnimation p;

	public AnimationTestFrame() {
		super("Animationtest");
		p = new LoginAnimation();
		add(p);
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public LoginAnimation getP() {
		return p;
	}

	public static void main(String[] args) {
		final TimingSource ts = new SwingTimerTimingSource();
		Animator.setDefaultTimingSource(ts);
		ts.init();

		AnimationTestFrame frame = new AnimationTestFrame();

		frame.setVisible(true);
		frame.getP().start();
	}
}
