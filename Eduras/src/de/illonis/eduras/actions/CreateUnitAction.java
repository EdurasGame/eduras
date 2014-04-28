package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.units.PlayerMainFigure;

public class CreateUnitAction extends RTSAction {
	private final static Logger L = EduLog.getLoggerFor(CreateUnitAction.class
			.getName());

	private NeutralBase baseToSpawnAt;
	private ObjectType typeOfUnitToSpawn;

	public CreateUnitAction(PlayerMainFigure executingPlayer,
			ObjectType typeOfUnit, NeutralBase baseToSpawnAt)
			throws WrongObjectTypeException {
		super(executingPlayer, -1);

		this.baseToSpawnAt = baseToSpawnAt;
		this.typeOfUnitToSpawn = typeOfUnit;

		if (!typeOfUnit.isUnit()) {
			throw new WrongObjectTypeException(typeOfUnit);
		}
	}

	@Override
	protected void executeAction(GameInformation info) {
		if (baseToSpawnAt.getCurrentOwnerTeam() == null
				|| !baseToSpawnAt.getCurrentOwnerTeam().equals(
						getExecutingPlayer())) {
			L.warning("Player tried to spawn a unit at a base that is not his own. Should already be caught on client.");
			return;
		}

		info.getEventTriggerer().createObjectAt(typeOfUnitToSpawn,
				baseToSpawnAt.getPositionVector(),
				getExecutingPlayer().getOwner());
	}
}
