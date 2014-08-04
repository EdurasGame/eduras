package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;

public abstract class ActionBarSubPage extends ActionBarPage {

	private final static Logger L = EduLog.getLoggerFor(ActionBarSubPage.class
			.getName());

	private ActionButton abortButton;

	public ActionBarSubPage(PageNumber id, PageNumber superiorPage,
			GamePanelReactor reactor, ActionBar actionBar) {
		super(id);
		abortButton = new AbortButton(reactor, actionBar, superiorPage);
		addButton(abortButton);
	}

	@Override
	public void removeAllButtons() {
		super.removeAllButtons();
		addButton(abortButton);
	}
}
