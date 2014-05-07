package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;

/**
 * This action spawns an item of a certain type at a certain position.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SpawnItemAction extends RTSAction {

	private final static Logger L = EduLog.getLoggerFor(SpawnItemAction.class
			.getName());

	private ObjectType itemType;
	private Vector2df spawnPos;

	/**
	 * Create a new SpawnItemAction.
	 * 
	 * @param executingPlayer
	 *            The player to perform the action.
	 * @param itemType
	 *            The type of the item to spawn. Must be an item type.
	 * @param position
	 *            the location to spawn the item at.
	 * @throws WrongObjectTypeException
	 *             Thrown if the given type is not an item.
	 */
	public SpawnItemAction(Player executingPlayer, ObjectType itemType,
			Vector2df position) throws WrongObjectTypeException {
		super(executingPlayer, -1);

		this.itemType = itemType;
		this.spawnPos = position;

		if (!itemType.isItem()) {
			throw new WrongObjectTypeException(itemType);
		}

		switch (itemType) {
		case ITEM_WEAPON_SIMPLE:
			costs = S.go_simpleweapon_costs;
			break;
		case ITEM_WEAPON_SNIPER:
			costs = S.go_sniperweapon_costs;
			break;
		case ITEM_WEAPON_SPLASH:
			costs = S.go_splashweapon_costs;
			break;
		case ITEM_WEAPON_SWORD:
			costs = S.go_swordweapon_costs;
			break;
		case ROCKETLAUNCHER:
			costs = S.go_rocketlauncher_costs;
			break;
		case MINELAUNCHER:
			costs = S.go_mineweapon_costs;
			break;
		case ASSAULTRIFLE:
			costs = S.go_assaultrifle_costs;
			break;
		default:
			throw new WrongObjectTypeException(itemType);
		}
	}

	@Override
	protected void executeAction(GameInformation info) {
		// check for visibility of position is performed on client
		info.getEventTriggerer().createObjectAt(itemType, spawnPos,
				GameObject.OWNER_WORLD);
	}
}
