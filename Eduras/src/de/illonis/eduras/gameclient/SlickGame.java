package de.illonis.eduras.gameclient;

import java.awt.Font;
import java.util.Map;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.gui.InputKeyHandler;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.game.GuiMouseHandler;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logic.LogicGameWorker;

public class SlickGame extends BasicGame {
	private final static Logger L = EduLog.getLoggerFor(SlickGame.class
			.getName());

	private TrueTypeFont defaultFont;

	private LogicGameWorker lgw;
	private final GuiMouseHandler mouseHandler;
	private final InputKeyHandler keyHandler;
	private GameRenderer renderer;

	private Map<Integer, GameObject> objs;

	public void setWorker(LogicGameWorker lgw, GameRenderer renderer) {
		this.lgw = lgw;
		this.renderer = renderer;
	}

	public SlickGame(Map<Integer, GameObject> objs, GuiMouseHandler handler,
			InputKeyHandler keyHandler) {
		super("Eduras?");
		this.mouseHandler = handler;
		this.keyHandler = keyHandler;
		this.objs = objs;
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
