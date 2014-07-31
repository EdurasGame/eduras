package de.illonis.eduras.gameclient.gui.hud.actionbar;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.hud.ActionButton;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SpawnItemButton extends ActionButton {

	private final static Logger L = EduLog.getLoggerFor(SpawnItemButton.class
			.getName());

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
	}

	@Override
	protected void actionPerformed() {
		reactor.setClickState(ClickState.SELECT_POSITION_FOR_ITEMSPAWN);
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentItemSpawnType(typeOfItemToSpawn);
	}

	@Override
	public String getLabel() {
		return "Spawn item " + typeOfItemToSpawn.name() + " [Costs: "
				+ typeOfItemToSpawn.getCosts() + "]";
	}
}
