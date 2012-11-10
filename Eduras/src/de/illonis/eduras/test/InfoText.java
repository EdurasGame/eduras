package de.illonis.eduras.test;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.MoveableGameObject;
import de.illonis.eduras.interfaces.Drawable;

/**
 * Class to display info on {@link GameWorldPanelTester}.
 * 
 * @author illonis
 * 
 */
public class InfoText extends GameObject implements Drawable {

	private final MoveableGameObject obj;

	public InfoText(GameInformation game, MoveableGameObject yc) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.GameObject#onCollision(de.illonis.eduras.GameObject)
	 */
	@Override
	public void onCollision(GameObject collidingObject) {
		// TODO Auto-generated method stub

	}
}
