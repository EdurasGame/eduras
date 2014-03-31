package de.illonis.eduras.gameclient;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.Sys;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
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

	private UnicodeFont defaultFont;

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

	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer container) throws SlickException {
		// GL11.glDis(GL11.GL_TEXTURE_2D);
		// GL11.glEnable(GL11.GL_BLEND);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		defaultFont = new UnicodeFont(new Font("Arial", Font.PLAIN, 12));
		defaultFont.addAsciiGlyphs();
		container.setMinimumLogicUpdateInterval(10);
		container.setMaximumLogicUpdateInterval(70);
		container.setTargetFrameRate(60);
		ColorEffect e = new ColorEffect();
		e.setColor(Color.white);
		defaultFont.getEffects().add(e);
		defaultFont.loadGlyphs();
		GraphicsPreLoader.preLoadImages();
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
		//System.out.println(getTime() + " elapsed " + delta);
		lgw.gameUpdate(delta);
	}
	
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
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
