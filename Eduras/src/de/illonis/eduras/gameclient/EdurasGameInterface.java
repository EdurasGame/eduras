package de.illonis.eduras.gameclient;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.ClientRole;

/**
 * Represents a gameclient for Eduras.
 * 
 * @author illonis
 * 
 */
public interface EdurasGameInterface {

	void render(GameContainer container, Graphics g) throws SlickException;

	void update(GameContainer container, int delta);

	/**
	 * Quits the game.
	 */
	void exit();

	public abstract void mouseClicked(int button, int x, int y, int clickCount);

	public abstract void mouseMoved(int oldx, int oldy, int newx, int newy);

	public abstract void mouseDragged(int oldx, int oldy, int newx, int newy);

	public abstract void mousePressed(int button, int x, int y);

	public abstract void mouseReleased(int button, int x, int y);

	public abstract void mouseWheelMoved(int change);

	/**
	 * Indicates that a key was pressed.
	 * 
	 * @param key
	 *            the key pressed (see {@link Input}).
	 * @param c
	 *            the key char.
	 */
	public void keyPressed(int key, char c);

	/**
	 * Indicates a key release.
	 * 
	 * @param key
	 *            the key code. See fields of {@link Input}.
	 * @param c
	 *            the key char.
	 */
	public void keyReleased(int key, char c);

	void setClientName(String username);

	void setRole(ClientRole role);

	NetworkManager getNetworkManager();

}
