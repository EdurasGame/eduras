package de.illonis.eduras.gameclient;

import java.util.Map;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logic.LogicGameWorker;

public class SlickGame extends BasicGame {
	private final static Logger L = EduLog.getLoggerFor(SlickGame.class
			.getName());

	private LogicGameWorker lgw;

	private Map<Integer, GameObject> objs;

	public void setWorker(LogicGameWorker lgw) {
		this.lgw = lgw;
	}

	public SlickGame(Map<Integer, GameObject> objs) {
		super("Eduras?");
		this.objs = objs;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		container.setClearEachFrame(true);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.setColor(new Color(1f, 0f, 1f));
		for (GameObject o : objs.values()) {
			if (o.getShape() != null) {
				g.fill(o.getShape());
			}
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		lgw.gameUpdate(delta);
	}
}
