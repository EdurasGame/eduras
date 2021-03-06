package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;

/**
 * Action bar that contains spells that cannot target units.
 * 
 * @author illonis
 * 
 */
public class SpellPage extends ActionBarPage {
	private final static Logger L = EduLog.getLoggerFor(SpellPage.class
			.getName());

	/**
	 * The spell bar
	 * 
	 * @param index
	 *            page index.
	 * 
	 * @param ui
	 *            ui
	 * @param reactor
	 *            reactor.
	 */
	public SpellPage(int index, UserInterface ui, GamePanelReactor reactor) {
		super(index, ui, reactor);
		generateButtonsForSpells();
	}

	private void generateButtonsForSpells() {
		addButton(new ScoutSpellButton(reactor));
	}
}
