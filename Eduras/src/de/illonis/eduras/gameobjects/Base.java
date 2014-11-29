package de.illonis.eduras.gameobjects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A NeutralBase is a {@link NeutralArea} that triggers {@link GameMode}
 * dependent behavior when occupied.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Base extends NeutralArea {

	private final static Logger L = EduLog.getLoggerFor(Base.class.getName());

	private final long resourceGenerateTimeInterval;
	private final int resourceGenerateAmount;
	private float resourceGenerateMultiplicator;

	/**
	 * Denotes whether a base belongs to team a, team b or is neutral when
	 * created.
	 * 
	 * @author Florian 'Ren' Mai
	 * 
	 */
	public enum BaseType {
		NEUTRAL, TEAM_A, TEAM_B;
	}

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
	public Base(GameInformation game, TimingSource timingSource, int id,
			float mult) {
		super(game, timingSource, id);
		setTimeNeeded(S.Server.neutralbase_overtaketime_default);
		resourceGenerateAmount = S.Server.neutralbase_resource_baseamount;
		resourceGenerateMultiplicator = mult;
		resourceGenerateTimeInterval = S.Server.neutralbase_resource_interval;
	}

	@Override
	protected void onNeutralAreaOccupied(Team occupyingTeam) {
		getGame().getGameSettings().getGameMode()
				.onBaseOccupied(this, occupyingTeam);
	}

	@Override
	protected Team determineProgressingTeam(GameObject object,
			boolean objectEntered) {
		return getGame()
				.getGameSettings()
				.getGameMode()
				.determineProgressingTeam(this, object, objectEntered,
						getPresentPlayers());
	}

	private Set<GameObject> getPresentPlayers() {
		HashSet<GameObject> unitsOnly = new HashSet<GameObject>();
		for (GameObject anyObject : getPresentObjects())
			if (anyObject instanceof PlayerMainFigure) {
				unitsOnly.add(anyObject);
			}
		return unitsOnly;
	}

	@Override
	protected void onNeutralAreaLost(Team losingTeam) {
		getGame().getGameSettings().getGameMode().onBaseLost(this, losingTeam);
	}

	/**
	 * Returns the amount of resources this base generates in one period of the
	 * interval without taking into account the multiplicator.
	 * 
	 * @return amount of resources
	 */
	public int getResourceGenerateAmount() {
		return resourceGenerateAmount;
	}

	/**
	 * Returns the amount of resources this base generates in one period of the
	 * interval.
	 * 
	 * @param bases
	 *            if the income is constant, these bases are used to calculate
	 *            the total income
	 * 
	 * @return amount of resources
	 */
	public int getResourceGenerateAmountPerTimeInterval(Collection<Base> bases) {
		if (bases == null || S.Server.gm_edura_constant_income) {
			float totalIncomePerSecond = calculateTotalIncomeGenerated(bases);
			float ratioOfTotalIncome = incomeOfBasePerSecond(this)
					/ totalIncomePerSecond;

			return Math
					.round((ratioOfTotalIncome * (S.Server.gm_edura_income_per_minute / 60f))
							* (getResourceGenerateTimeInterval() / 1000));
		} else {
			return Math.round(resourceGenerateAmount
					* resourceGenerateMultiplicator);
		}
	}

	private float incomeOfBasePerSecond(Base base) {
		return (((float) Math.round(resourceGenerateAmount
				* resourceGenerateMultiplicator) / base
				.getResourceGenerateTimeInterval()) * 1000f);
	}

	// calculates how much is generated per second
	private float calculateTotalIncomeGenerated(Collection<Base> bases) {
		int totalIncome = 0;
		for (Base base : bases) {

			// calculate how much the base generates per second here
			totalIncome += incomeOfBasePerSecond(base);
		}
		return totalIncome;
	}

	/**
	 * Get the interval in which resources are generated.
	 * 
	 * @return interval
	 */
	public long getResourceGenerateTimeInterval() {
		return resourceGenerateTimeInterval;
	}

	/**
	 * Returns the factor the generated amount is multiplied by.
	 * 
	 * @return multiplicator
	 */
	public float getResourceGenerateMultiplicator() {
		return resourceGenerateMultiplicator;
	}

	/**
	 * set the factor the generated amount is mutliplied by.
	 * 
	 * @param resourceGenerateMultiplicator
	 */
	public void setResourceGenerateMultiplicator(
			float resourceGenerateMultiplicator) {
		this.resourceGenerateMultiplicator = resourceGenerateMultiplicator;
	}
}
