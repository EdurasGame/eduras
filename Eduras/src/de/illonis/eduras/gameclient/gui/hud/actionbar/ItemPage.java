package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;

/**
 * Actionbar page that holds buttons to spawn items.
 * 
 * @author illonis
 * 
 */
public class ItemPage extends ActionBarPage {

	private final static Logger L = EduLog.getLoggerFor(ItemPage.class
			.getName());

	public ItemPage(int index, UserInterface ui, GamePanelReactor reactor) {
		super(index, ui, reactor);
		generateButtons();
		updateBounds();
	}

	private void generateButtons() {
		for (ObjectType type : ObjectType.getItemTypes()) {
			SpawnItemButton button = new SpawnItemButton(type, reactor);
			addButton(button);
		}
	}

}
