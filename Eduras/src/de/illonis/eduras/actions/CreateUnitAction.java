package de.illonis.eduras.actions;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * An {@link RTSAction} that can be used for creating a unit.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CreateUnitAction extends RTSAction {
	private final static Logger L = EduLog.getLoggerFor(CreateUnitAction.class
			.getName());

	private Base baseToSpawnAt;
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
			Base baseToSpawnAt) throws WrongObjectTypeException {
		super(executingPlayer, -1);

		this.baseToSpawnAt = baseToSpawnAt;
		this.typeOfUnitToSpawn = typeOfUnit;

		if (!typeOfUnit.isUnit()) {
			throw new WrongObjectTypeException(typeOfUnit);
		}

		switch (typeOfUnit) {
		case OBSERVER:
			costs = S.Server.unit_observer_costs;
			break;
		default:
			break;
		}
	}

	@Override
	protected void executeAction(GameInformation info) {
		Team teamOfExecutingPlayer;
		try {
			teamOfExecutingPlayer = getExecutingPlayer().getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.warning("Player doesn't have a team although he should as he's creating a unit!");
			return;
		}

		if (baseToSpawnAt.getCurrentOwnerTeam() == null
				|| !baseToSpawnAt.getCurrentOwnerTeam().equals(
						teamOfExecutingPlayer)) {
			L.warning("Player tried to spawn a unit at a base that is not his own. Should already be caught on client.");
			return;
		}

		// info.getEventTriggerer().createObjectIn(typeOfUnitToSpawn,
		// baseToSpawnAt.getShape(), getExecutingPlayer().getPlayerId());
		int unitId = info.getEventTriggerer().createObjectAtBase(
				typeOfUnitToSpawn, baseToSpawnAt,
				getExecutingPlayer().getPlayerId());
		Unit createdUnit;
		try {
			createdUnit = (Unit) info.findObjectById(unitId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Couldn't create object!!", e);
			return;
		}

		info.getEventTriggerer().setTeamOfUnit(createdUnit,
				teamOfExecutingPlayer);
	}
}
