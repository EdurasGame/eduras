package de.illonis.eduras.gameclient;

import java.util.Map;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameobjects.GameObject;

public class SlickGame implements Game {
	private final static Logger L = EduLog.getLoggerFor(SlickGame.class
			.getName());
	private Map<Integer, GameObject> objs;

	public SlickGame(Map<Integer, GameObject> objs) {
		this.objs = objs;
	}

	@Override
	public boolean closeRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTitle() {
		return "the title";
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}
}
