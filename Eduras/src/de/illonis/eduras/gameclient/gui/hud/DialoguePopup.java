package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;

public abstract class DialoguePopup extends ClickableGuiElement {

	private final static Logger L = EduLog.getLoggerFor(DialoguePopup.class
			.getName());

	private final String text;
	private Rectangle bounds;
	private Font font;
	private LinkedList<AnswerButton> answerButtons;

	protected DialoguePopup(UserInterface gui, String text) {
		super(gui);

		this.text = text;
		answerButtons = new LinkedList<AnswerButton>();
		bounds = new Rectangle(0, 0, 300, 60);
	}

	@Override
	public abstract Rectangle getBounds();

	@Override
	public void render(Graphics g) {
		if (isVisible()) {
			if (font == null) {
				font = FontCache.getFont(FontKey.DEFAULT_FONT, g);

				int buttonWidthSum = 0;
				for (AnswerButton answerButton : answerButtons) {
					answerButton.setSize(font);
					buttonWidthSum += answerButton.getWidth() + 10;
				}
				int width = Math.max(buttonWidthSum, font.getWidth(text)) + 30;
				bounds.setWidth(width);
				int height = font.getLineHeight() * 2 + 30;
				bounds.setHeight(height);
				bounds.setLocation(screenX, screenY);

				buttonWidthSum = 5;
				for (int i = 0; i < answerButtons.size(); i++) {
					AnswerButton answerButton = answerButtons.get(i);

					answerButton.setLocation(screenX + buttonWidthSum, screenY
							+ height);

					buttonWidthSum += 10;
				}
			}
			g.setColor(Color.white);
			g.fill(bounds);

			for (AnswerButton answerButton : answerButtons) {
				answerButton.render(g, font);
			}
			font.drawString(screenX + 10, screenY + 5, text, Color.black);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = (newWidth - bounds.getWidth()) / 2;
		screenY = (newHeight - bounds.getHeight()) / 2;
	}

	protected void addAnswer(AnswerButton answer) {

	}

	abstract class AnswerButton {
		private Rectangle answerRect;
		private final String text;

		protected AnswerButton(String text) {
			this.text = text;
		}

		private void render(Graphics g, Font font) {

			g.setColor(Color.black);
			g.fill(answerRect);
			g.setColor(Color.yellow);
			font.drawString(answerRect.getX() + 5, answerRect.getY() + 5, text);
		}

		private void setSize(Font font) {
			answerRect.setSize(font.getWidth(text) + 10,
					font.getLineHeight() + 10);
		}

		private void setLocation(float x, float y) {
			answerRect.setLocation(x, y - answerRect.getHeight() - 10);
		}

		private int getWidth() {
			return (int) answerRect.getWidth();
		}

		protected abstract void onClick();
	}
}
