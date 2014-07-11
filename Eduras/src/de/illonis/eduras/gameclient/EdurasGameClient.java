package de.illonis.eduras.gameclient;

import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.gui.GameManager;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory;
import de.illonis.eduras.gameclient.gui.game.GuiMouseHandler;
import de.illonis.eduras.gameclient.gui.game.InputKeyHandler;
import de.illonis.eduras.logic.LogicGameWorker;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.ClientRole;

public class EdurasGameClient implements EdurasGameInterface {

	private final static Logger L = EduLog.getLoggerFor(EdurasGameClient.class
			.getName());

	private final GameClient client;
	private final InputKeyHandler keyHandler;
	private final GuiMouseHandler mouseHandler;
	private LogicGameWorker worker;

	/**
	 * Creates a new client and initializes all necessary components.
	 * 
	 * @param clientFrame
	 *            the parent frame.
	 */
	public EdurasGameClient(GameManager clientFrame, GameContainer container) {
		client = new GameClient(clientFrame, container);
		keyHandler = client.getLogic().getKeyHandler();
		mouseHandler = client.getLogic().getMouseHandler();
		worker = EdurasInitializer.getInstance().getLogic().startWorker();
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		client.getLogic().getRenderer().render(container, g);
	}

	@Override
	public void update(GameContainer container, int delta) {
		worker.gameUpdate(delta);
		for (ParticleSystem system : EffectFactory.getSystems().values()) {
			synchronized (system) {
				system.update(delta);
			}
		}
	}

	@Override
	public void exit() {
		client.getLogic().onGameQuit();
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
	public void mousePressed(int button, int x, int y) {
		mouseHandler.mousePressed(button, x, y);
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

	@Override
	public void setClientName(String username) {
		client.setClientName(username);
	}

	@Override
	public void setRole(ClientRole role) {
		client.setRole(role);
	}

	@Override
	public NetworkManager getNetworkManager() {
		return client.getNetworkManager();
	}

	@Override
	public void init() {
		client.sendInitInformation();
	}

}
