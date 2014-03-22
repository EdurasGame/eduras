package de.illonis.eduras.gameclient.gui.game;

import java.awt.Color;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

/**
 * The panel where the game is drawn on.
 * 
 * @author illonis
 * 
 */
public class GamePanel extends CanvasGameContainer {

	private static final long serialVersionUID = 1L;

	GamePanel(Game game) throws SlickException {
		super(game);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.black);
		//setIgnoreRepaint(true);
	}
}
