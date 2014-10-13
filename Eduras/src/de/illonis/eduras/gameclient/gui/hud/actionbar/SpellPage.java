package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBar;
import de.illonis.eduras.gameclient.gui.hud.ActionBarSubPage;

public class SpellPage extends ActionBarSubPage {
	private final static Logger L = EduLog.getLoggerFor(SpellPage.class
			.getName());

	public SpellPage(GamePanelReactor reactor, ActionBar actionBar) {
		super(PageNumber.SPELL, PageNumber.MAIN, reactor, actionBar);

		generateButtonsForSpells(reactor);
	}

	private void generateButtonsForSpells(GamePanelReactor reactor) {
		addButton(new HealButton(reactor));
		addButton(new SpeedSpellButton(reactor));
		addButton(new InvisibilitySpellButton(reactor));
		addButton(new BlinkSpellButton(reactor));
	}
}
