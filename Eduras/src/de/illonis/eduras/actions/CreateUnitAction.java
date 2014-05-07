package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.settings.S;

/**
 * An {@link RTSAction} that can be used for creating a unit.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CreateUnitAction extends RTSAction {
	private final static Logger L = EduLog.getLoggerFor(CreateUnitAction.class
			.getName());

	private NeutralBase baseToSpawnAt;
	private ObjectType typeOfUnitToSpawn;

	/**
	 * Instantiates a CreateUnitAction.
	 * 
	 * @param executingPlayer
	 *            The player who wants to create a unit.
	 * @param typeOfUnit
	 *            The type of unit to create.
	 * @param baseToSpawnAt
	 *            The base to spawn the new unit at.
	 * @throws WrongObjectTypeException
	 *             Thrown if the given object type cannot be spawned.
	 */
	public CreateUnitAction(Player executingPlayer, ObjectType typeOfUnit,
			NeutralBase baseToSpawnAt) throws WrongObjectTypeException {
		super(executingPlayer, -1);

		this.baseToSpawnAt = baseToSpawnAt;
		this.typeOfUnitToSpawn = typeOfUnit;

		if (!typeOfUnit.isUnit()) {
			throw new WrongObjectTypeException(typeOfUnit);
		}

		switch (typeOfUnit) {
		case OBSERVER:
			costs = S.unit_observer_costs;
			break;
		default:
			break;
		}
	}

	@Override
	protected void executeAction(GameInformation info) {
		if (baseToSpawnAt.getCurrentOwnerTeam() == null
				|| !baseToSpawnAt.getCurrentOwnerTeam().equals(
						getExecutingPlayer().getTeam())) {
			L.warning("Player tried to spawn a unit at a base that is not his own. Should already be caught on client.");
			return;
		}

		info.getEventTriggerer().createObjectAt(typeOfUnitToSpawn,
				baseToSpawnAt.getPositionVector(),
				getExecutingPlayer().getPlayerId());
	}
}
