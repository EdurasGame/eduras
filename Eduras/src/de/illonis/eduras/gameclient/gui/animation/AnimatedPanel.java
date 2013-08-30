package de.illonis.eduras.gameclient.gui.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedPanel extends JPanel implements ActionListener {

	private int x, y, w, h;

	int animationDuration = 2000;
	long animStartTime;

	public AnimatedPanel() {
		setPreferredSize(new Dimension(500, 500));
		x = y = 0;
		w = h = 10;
		Timer timer = new Timer(30, this);
		timer.setInitialDelay(200);
		animStartTime = 200 + System.nanoTime() / 1000000;
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GREEN);
		g2d.fillRect(x, y, w, h);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		x++;
		y++;
		repaint();
	}
}
