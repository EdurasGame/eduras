package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;

public class ItemPage extends ActionBarPage {

	private final static Logger L = EduLog.getLoggerFor(ItemPage.class
			.getName());

	public ItemPage(GamePanelReactor reactor) {
		super(PageNumber.ITEMS);

		generateButtons(reactor);
	}

	private void generateButtons(GamePanelReactor reactor) {
		for (ObjectType type : ObjectType.getItemTypes()) {
			SpawnItemButton button = new SpawnItemButton(type, reactor);
			addButton(button);
		}
	}

}