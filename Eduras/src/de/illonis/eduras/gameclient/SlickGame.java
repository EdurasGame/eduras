package de.illonis.eduras.gameclient;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import de.illonis.eduras.gameclient.gui.InputKeyHandler;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.game.GuiMouseHandler;
import de.illonis.eduras.logic.LogicGameWorker;

/**
 * Contains Slick implementation for Eduras.
 * 
 * @author illonis
 * 
 */
public class SlickGame extends BasicGame {

	private TrueTypeFont defaultFont;

	private LogicGameWorker lgw;
	private final GuiMouseHandler mouseHandler;
	private final InputKeyHandler keyHandler;
	private GameRenderer renderer;

	/**
	 * Associates logic and renderer that will be notified.
	 * 
	 * @param lgw
	 *            the logic game worker.
	 * @param renderer
	 *            the renderer.
	 */
	public void setWorker(LogicGameWorker lgw, GameRenderer renderer) {
		this.lgw = lgw;
		this.renderer = renderer;
	}

	/**
	 * Creates a new game.
	 * 
	 * @param mouseHandler
	 *            the mouseHandler.
	 * @param keyHandler
	 *            the keyHandler.
	 */
	public SlickGame(GuiMouseHandler mouseHandler, InputKeyHandler keyHandler) {
		super("Eduras?");
		this.mouseHandler = mouseHandler;
		this.keyHandler = keyHandler;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		defaultFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), true);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.setFont(defaultFont);
		renderer.render(container, g);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		lgw.gameUpdate(delta);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		mouseHandler.mousePressed(button, x, y);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		mouseHandler.mouseClicked(button, x, y, clickCount);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		mouseHandler.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		mouseHandler.mouseDragged(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		mouseHandler.mouseReleased(button, x, y);
	}

	@Override
	public void mouseWheelMoved(int change) {
		mouseHandler.mouseWheelMoved(change);
	}

	@Override
	public void keyPressed(int key, char c) {
		keyHandler.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		keyHandler.keyReleased(key, c);
	}
}
