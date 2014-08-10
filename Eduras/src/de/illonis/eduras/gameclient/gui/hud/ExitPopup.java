package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class ExitPopup extends ClickableGuiElement {

	private final static int WIDTH = 300;
	private final static int HEIGHT = 100;
	private final Rectangle bounds;
	private final Rectangle yesRect;
	private final Rectangle noRect;
	private boolean visible;
	private final String text = "Do you really want to exit?";

	UserInterface ui;

	protected ExitPopup(UserInterface gui) {
		super(gui);
		this.ui = gui;
		bounds = new Rectangle(0, 0, WIDTH, HEIGHT);
		yesRect = new Rectangle(0, 0, 100, 30);
		noRect = new Rectangle(0, 0, 100, 30);
		visible = false;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean onClick(Vector2f p) {
		if (!visible)
			return false;

		if (yesRect.contains(p.x, p.y)) {
			// exit
			getMouseHandler().exitRequested();
		} else if (noRect.contains(p.x, p.y)) {
			visible = false;
		}
		return true;
	}

	@Override
	public Rectangle getBounds() {

		return bounds;
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			g.setColor(Color.white);
			g.fill(bounds);
			g.setColor(Color.black);
			g.fill(yesRect);
			g.fill(noRect);
			g.setColor(Color.black);
			g.drawString(text, screenX + 5, screenY + 5);
			g.setColor(Color.yellow);
			g.drawString("Yes", yesRect.getX() + 5, yesRect.getY() + 5);
			g.drawString("No", noRect.getX() + 5, noRect.getY() + 5);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = (newWidth - WIDTH) / 2;
		screenY = (newHeight - HEIGHT) / 2;
		bounds.setLocation(screenX, screenY);
		yesRect.setLocation(screenX + 30, screenY + HEIGHT - 50);
		noRect.setLocation(screenX + WIDTH - 150, screenY + HEIGHT - 50);
	}

	@Override
	public boolean isActive() {
		return super.isActive() && visible;
	}
}
