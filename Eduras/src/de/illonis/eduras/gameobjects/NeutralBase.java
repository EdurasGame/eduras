package de.illonis.eduras.gameobjects;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.settings.S;

/**
 * A NeutralBase is a {@link NeutralArea} that triggers {@link GameMode}
 * dependent behavior when occupied.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
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
		getGame().getGameSettings().getGameMode().onBaseOccupied(occupyingTeam);
	}

	@Override
	protected Team determineProgressingTeam(GameObject object,
			boolean objectEntered) {
		return getGame()
				.getGameSettings()
				.getGameMode()
				.determineProgressingTeam(object, objectEntered,
						getPresentUnits());
	}

	private Set<GameObject> getPresentUnits() {
		HashSet<GameObject> unitsOnly = new HashSet<GameObject>();
		for (GameObject anyObject : getPresentObjects())
			if (anyObject.isUnit()) {
				unitsOnly.add(anyObject);
			}
		return unitsOnly;
	}

	@Override
	protected void onNeutralAreaLost(Team losingTeam) {
		getGame().getGameSettings().getGameMode().onBaseLost(losingTeam);
	}
}
