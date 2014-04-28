package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;

/**
 * Displays resurrection buttons.
 * 
 * @author illonis
 * 
 */
public class ResurrectPage extends ActionBarPage {

	private final static Logger L = EduLog.getLoggerFor(ResurrectPage.class
			.getName());

	public ResurrectPage() {
		super(PageNumber.RESURRECT);

	}
}
