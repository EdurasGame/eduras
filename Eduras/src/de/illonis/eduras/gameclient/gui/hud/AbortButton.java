package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage.PageNumber;

/**
 * An abort button is a button that lets you go back to the upper page.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class AbortButton extends ActionButton {

	private final static Logger L = EduLog.getLoggerFor(AbortButton.class
			.getName());

	private ActionBar actionBar;
	private PageNumber targetPage;

	/**
	 * Create a new AbortButton.
	 * 
	 * @param reactor
	 * @param actionBar
	 * @param targetPage
	 */
	public AbortButton(GamePanelReactor reactor, ActionBar actionBar,
			PageNumber targetPage) {
		super(ImageKey.ACTION_ABORT, reactor);
		this.actionBar = actionBar;
		this.targetPage = targetPage;
	}

	@Override
	public void actionPerformed() {
		reactor.setClickState(ClickState.DEFAULT);
		actionBar.setPage(targetPage);
	}

	@Override
	public String getLabel() {
		return "abort";
	}
}
