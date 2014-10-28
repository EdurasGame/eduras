package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;

/**
 * Action bar page that contains spells that can be cast on players and units.
 * 
 * @author illonis
 * 
 */
public class UnitSpellPage extends ActionBarPage {
	private final static Logger L = EduLog.getLoggerFor(UnitSpellPage.class
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
	public UnitSpellPage(int index, UserInterface ui, GamePanelReactor reactor) {
		super(index, ui, reactor);
		generateButtonsForSpells();
	}

	private void generateButtonsForSpells() {
		addButton(new HealButton(reactor));
		addButton(new SpeedSpellButton(reactor));
		addButton(new InvisibilitySpellButton(reactor));
		addButton(new BlinkSpellButton(reactor));
		addButton(new RezzButton(reactor));
	}
}
