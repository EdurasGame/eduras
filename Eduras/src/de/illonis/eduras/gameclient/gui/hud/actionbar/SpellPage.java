package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;

public class SpellPage extends ActionBarPage {
	private final static Logger L = EduLog.getLoggerFor(SpellPage.class
			.getName());

	/**
	 * The spell bar
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
		addButton(new HealButton(reactor));
		addButton(new SpeedSpellButton(reactor));
		addButton(new InvisibilitySpellButton(reactor));
		addButton(new BlinkSpellButton(reactor));
		addButton(new SpawnObserverButton(reactor));
		addButton(new ScoutSpellButton(reactor));
	}
}
