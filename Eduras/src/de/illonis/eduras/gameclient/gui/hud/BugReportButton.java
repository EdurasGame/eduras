package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.bugreport.BugReportFrame;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

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
				InteractMode.MODE_STRATEGY);
	}

	@Override
	public boolean onClick(Point p) {
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
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLUE);
		g2d.fill(bounds);
		g2d.setColor(Color.WHITE);
		g2d.drawString(buttonText, screenX + 10, screenY + height - 5);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = 0;
		bounds.setLocation(screenX, screenY);
	}

	@Override
	public void onPlayerInformationReceived() {
	}

}
