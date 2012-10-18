package de.illonis.eduras;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Class to display info on {@link GameWorldPanelTester}.
 * 
 * @author illonis
 * 
 */
public class InfoText extends GameObject {

	private MoveableGameObject obj;

	public InfoText(MoveableGameObject yc) {
		super();
		this.obj = yc;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawString(
				"Speed: "
						+ obj.getSpeed()
						+ " ~ Left-Click: increase speed ~ Right-Click: decrease speed",
				xPosition, yPosition);
	}
}
