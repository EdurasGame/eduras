package de.illonis.eduras.gameclient.gui.hud.actionbar;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SpawnItemButton extends ActionButton {

	private ObjectType typeOfItemToSpawn;

	/**
	 * Create a {@link SpawnItemButton}.
	 * 
	 * @param type
	 *            The object type that is going to be spawned when the button is
	 *            clicked.
	 * @param reactor
	 *            GUI logic to call when clicked
	 */
	public SpawnItemButton(ObjectType type, GamePanelReactor reactor) {
		super(ImageKey.typeToImageKey(type), reactor);
		this.typeOfItemToSpawn = type;
		label = Localization.getStringF(
				"Client.gui.actions.spell_spawnitem_title",
				typeOfItemToSpawn.name());
		description = Localization.getStringF(
				"Client.gui.actions.spell_spawnitem_text",
				typeOfItemToSpawn.name());
	}

	@Override
	protected void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_POSITION_FOR_ITEMSPAWN);
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentItemSpawnType(typeOfItemToSpawn);
	}

	@Override
	public int getCosts() {
		return typeOfItemToSpawn.getCosts();
	}
}
