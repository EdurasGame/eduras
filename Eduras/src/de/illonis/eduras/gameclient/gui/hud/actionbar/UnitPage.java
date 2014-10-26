package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;

/**
 * Action bar page that contains unit spawn actions.
 * 
 * @author illonis
 * 
 */
public class UnitPage extends ActionBarPage {
	private final static Logger L = EduLog.getLoggerFor(UnitPage.class
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
	public UnitPage(int index, UserInterface ui, GamePanelReactor reactor) {
		super(index, ui, reactor);
		generateButtonsForSpells();
	}

	private void generateButtonsForSpells() {
		addButton(new SpawnObserverButton(reactor));
	}
}
