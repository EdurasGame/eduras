package de.illonis.eduras.gameclient;

import java.io.IOException;
import java.net.InetAddress;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import de.illonis.eduras.networking.ClientRole;

/**
 * Represents a gameclient for Eduras and abstracts the game logic from the
 * gui-menu-logic.
 * 
 * @author illonis
 * 
 */
public interface EdurasGameInterface {

	/**
	 * Renders the game.
	 * 
	 * @param container
	 *            target gamecontainer.
	 * @param g
	 *            target graphics.
	 * @throws SlickException
	 *             when rendering failed.
	 */
	void render(GameContainer container, Graphics g) throws SlickException;

	/**
	 * Updates gamelogic.
	 * 
	 * @param container
	 *            the gamecontainer.
	 * @param delta
	 *            step in ms.
	 */
	void update(GameContainer container, int delta);

	/**
	 * Sends init information to server.
	 */
	void init();

	/**
	 * Quits the game.
	 */
	void exit();

	/**
	 * Notification that mouse was clicked.
	 * 
	 * @param button
	 *            mouse button used.
	 * @param x
	 *            x position of mouse.
	 * @param y
	 *            y position of mouse
	 * @param clickCount
	 *            The number of times the button was clicked.
	 */
	public abstract void mouseClicked(int button, int x, int y, int clickCount);

	/**
	 * Notification that a mouse button was pressed
	 * 
	 * @param button
	 *            The index of the button (starting at 0)
	 * @param x
	 *            The x position of the mouse when the button was pressed
	 * @param y
	 *            The y position of the mouse when the button was pressed
	 */
	public abstract void mousePressed(int button, int x, int y);

	/**
	 * Notification that a mouse button was released
	 * 
	 * @param button
	 *            The index of the button (starting at 0)
	 * @param x
	 *            The x position of the mouse when the button was released
	 * @param y
	 *            The y position of the mouse when the button was released
	 */
	public abstract void mouseReleased(int button, int x, int y);

	/**
	 * Notification that mouse cursor was moved
	 * 
	 * @param oldx
	 *            The old x position of the mouse
	 * @param oldy
	 *            The old y position of the mouse
	 * @param newx
	 *            The new x position of the mouse
	 * @param newy
	 *            The new y position of the mouse
	 */
	public abstract void mouseMoved(int oldx, int oldy, int newx, int newy);

	/**
	 * Notification that mouse cursor was dragged
	 * 
	 * @param oldx
	 *            The old x position of the mouse
	 * @param oldy
	 *            The old y position of the mouse
	 * @param newx
	 *            The new x position of the mouse
	 * @param newy
	 *            The new y position of the mouse
	 */
	public abstract void mouseDragged(int oldx, int oldy, int newx, int newy);

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

	/**
	 * Notification that the mouse wheel position was updated
	 * 
	 * @param change
	 *            The amount of the wheel has moved
	 */
	public abstract void mouseWheelMoved(int change);

	/**
	 * Sets the clientname to use.
	 * 
	 * @param username
	 *            this clients username.
	 */
	void setClientName(String username);

	/**
	 * Sets the client role to use.
	 * 
	 * @param role
	 *            the client role.
	 */
	void setRole(ClientRole role);

	/**
	 * Connects to given server.
	 * 
	 * @param addr
	 *            server address.
	 * @param port
	 *            server port.
	 * @throws IOException
	 *             when connection failed.
	 */
	void connect(InetAddress addr, int port) throws IOException;

}
