package de.illonis.eduras.test;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.Game;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.MoveableGameObject;

/**
 * Class to display info on {@link GameWorldPanelTester}.
 * 
 * @author illonis
 * 
 */
public class InfoText extends GameObject {

	private MoveableGameObject obj;

	public InfoText(Game game, MoveableGameObject yc) {
		super(game);
		this.obj = yc;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawString(
				"Speed: "
						+ obj.getSpeed()
						+ " ~ Left-Click: increase speed ~ Right-Click: decrease speed",
				getDrawX(), getDrawY());
	}
}
