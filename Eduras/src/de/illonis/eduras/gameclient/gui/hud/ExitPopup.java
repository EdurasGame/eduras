package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;

/**
 * An exit confirmation popup.
 * 
 * @author illonis
 * 
 */
public class ExitPopup extends ClickableGuiElement {

	private final Rectangle bounds;
	private final Rectangle yesRect;
	private final Rectangle noRect;
	private final String text = "Do you really want to exit?";
	private Font font;

	protected ExitPopup(UserInterface gui) {
		super(gui);
		bounds = new Rectangle(0, 0, 300, 60);
		yesRect = new Rectangle(0, 0, 100, 30);
		noRect = new Rectangle(0, 0, 100, 30);
		setVisible(false);
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {

		if (!isVisible())
			return false;

		if (yesRect.contains(x, y)) {
			// exit
			getMouseHandler().exitRequested();
		} else if (noRect.contains(x, y)) {
			setVisible(false);
		}
		return true;
	}

	@Override
	public Rectangle getBounds() {

		return bounds;
	}

	@Override
	public void render(Graphics g) {
		if (isVisible()) {
			if (font == null) {
				font = FontCache.getFont(FontKey.DEFAULT_FONT, g);
				yesRect.setSize(font.getWidth("Yes") + 10,
						font.getLineHeight() + 10);
				noRect.setSize(font.getWidth("Yes") + 10,
						font.getLineHeight() + 10);
				int width = Math.max((int) yesRect.getWidth(),
						font.getWidth(text)) + 30;
				bounds.setWidth(width);
				int height = font.getLineHeight() * 2 + 30;
				bounds.setHeight(height);
				bounds.setLocation(screenX, screenY);
				yesRect.setLocation(screenX + 10,
						screenY + height - yesRect.getHeight() - 10);
				noRect.setLocation(screenX + width - noRect.getWidth() - 10,
						screenY + height - noRect.getHeight() - 10);
			}
			g.setColor(Color.white);
			g.fill(bounds);
			g.setColor(Color.black);
			g.fill(yesRect);
			g.fill(noRect);
			font.drawString(screenX + 10, screenY + 5, text, Color.black);
			g.setColor(Color.yellow);
			font.drawString(yesRect.getX() + 5, yesRect.getY() + 5, "Yes");
			font.drawString(noRect.getX() + 5, noRect.getY() + 5, "No");
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = (newWidth - bounds.getWidth()) / 2;
		screenY = (newHeight - bounds.getHeight()) / 2;
	}

}
