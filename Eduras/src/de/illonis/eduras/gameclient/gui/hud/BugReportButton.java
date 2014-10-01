package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.bugreport.BugReportFrame;
import de.illonis.eduras.units.InteractMode;

/**
 * A button that opens a bug report window.
 * 
 * @author illonis
 * 
 */
public class BugReportButton extends ClickableGuiElement {

	private final static Logger L = EduLog.getLoggerFor("BugReportButton");

	private final static String buttonText = "Report bug";
	private final int width;
	private final int height;
	private final Rectangle bounds;
	private final UserInterface gui;

	protected BugReportButton(UserInterface gui) {
		super(gui);
		this.gui = gui;
		width = 80;
		height = 20;
		bounds = new Rectangle(screenX, screenY, width, height);
		setActiveInteractModes(InteractMode.MODE_EGO,
				InteractMode.MODE_STRATEGY, InteractMode.MODE_DEAD);
	}

	@Override
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		openBugWindow();
		return true;
	}

	private void openBugWindow() {
		L.info("open bug");
		new BugReportFrame().show(gui);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fill(bounds);
		g.setColor(Color.white);
		g.drawString(buttonText, screenX + 10, screenY + height - 15);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 100;
		screenY = 0;
		bounds.setLocation(screenX, screenY);
	}

}
