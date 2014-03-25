package de.illonis.eduras.gameobjects;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

public class NeutralBase extends NeutralArea {

	private final static Logger L = EduLog.getLoggerFor(NeutralBase.class
			.getName());

	private final long resourceGenerateTimeInterval;
	private final int resourceGenerateAmount;

	/**
	 * @param game
	 *            game info.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            object id.
	 * @param mult
	 *            multiplcator for resource generation amount.
	 */
	public NeutralBase(GameInformation game, TimingSource timingSource, int id,
			int mult) {
		super(game, timingSource, id);
		// TODO Auto-generated constructor stub
		setTimeNeeded(S.neutralbase_overtaketime_default);
		resourceGenerateAmount = S.neutralbase_resource_baseamount * mult;
		resourceGenerateTimeInterval = S.neutralbase_resource_interval;
	}

	@Override
	protected void onNeutralAreaOccupied(Team occupyingTeam) {
		L.info("Team " + occupyingTeam.getName() + " occupied the base!");
	}

	@Override
	protected Team determineProgressingTeam(GameObject object,
			boolean objectEntered) {
		if (objectEntered) {
			if (object.isUnit()) {
				Unit u = (Unit) object;
				return u.getTeam();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	protected void onNeutralAreaLost(Team losingTeam) {
		L.info("Team " + losingTeam.getName() + " lost the base!");
	}
}
