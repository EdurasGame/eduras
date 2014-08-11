package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

public abstract class UnitSpellButton extends ActionButton {

	private final static Logger L = EduLog.getLoggerFor(UnitSpellButton.class
			.getName());

	private GameEventNumber spell;

	public UnitSpellButton(ImageKey image, GamePanelReactor reactor,
			GameEventNumber spell) {
		super(image, reactor);

		this.spell = spell;
	}

	@Override
	public final void actionPerformed() {
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentSpellSelected(spell);
		reactor.setClickState(ClickState.SELECT_TARGET_FOR_SPELL);
	}

	public GameEventNumber getSpell() {
		return spell;
	}
}
