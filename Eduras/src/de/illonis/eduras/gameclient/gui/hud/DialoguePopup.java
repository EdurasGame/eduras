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

public abstract class DialoguePopup extends ClickableGuiElement implements
		Cancelable {

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
		setVisible(false);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
		if (isVisible()) {

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

			buttonWidthSum = 10;
			for (int i = 0; i < answerButtons.size(); i++) {
				AnswerButton answerButton = answerButtons.get(i);

				answerButton.setLocation(screenX + buttonWidthSum, screenY
						+ height);

				buttonWidthSum += 10 + answerButton.getWidth();
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
		answerButtons.add(answer);
	}

	@Override
	public boolean mousePressed(int button, int x, int y) {
		return true;
	}

	@Override
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		return true;
	}

	@Override
	public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
		return true;
	}

	@Override
	public boolean mouseDragged(int oldx, int oldy, int newx, int newy) {
		return true;
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {

		if (!isVisible())
			return false;

		for (AnswerButton answerButton : answerButtons) {
			if (answerButton.contains(x, y)) {
				answerButton.onClick();
			}
		}
		return true;
	}

	protected void clearAnswers() {
		answerButtons.clear();
	}

	abstract class AnswerButton {
		private Rectangle answerRect;
		private final String text;

		protected AnswerButton(String text) {
			this.text = text;
			this.answerRect = new Rectangle(0, 0, 30, 30);
		}

		public boolean contains(int x, int y) {
			return answerRect.contains(x, y);
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

	@Override
	public boolean cancel() {
		if (!isVisible()) {
			return false;
		} else {
			setVisible(false);
			return true;
		}
	}
}
