package de.illonis.eduras.gamemodes;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * A game mode like deathmatch, but the user gets extra points when he conquers
 * neutralbases.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class KingOfTheHill extends Deathmatch {

	private HashMap<Base, Timer> neutralBasePointsAdderTimer;

	/**
	 * Create a new KingOfTheHill mode.
	 * 
	 * @param gameInfo
	 *            gameinfo to work with
	 */
	public KingOfTheHill(GameInformation gameInfo) {
		super(gameInfo);

		neutralBasePointsAdderTimer = new HashMap<Base, Timer>();
	}

	private final static Logger L = EduLog.getLoggerFor(KingOfTheHill.class
			.getName());

	@Override
	public GameModeNumber getNumber() {
		return GameModeNumber.KING_OF_THE_HILL;
	}

	@Override
	public String getName() {
		return "KingOfTheHill";
	}

	@Override
	public void onBaseOccupied(Base base, Team occupyingTeam) {
		Player player = occupyingTeam.getPlayers().getFirst();
		Timer timerForBase = new Timer();
		neutralBasePointsAdderTimer.put(base, timerForBase);
		timerForBase.schedule(new NeutralBasePointsAdder(player),
				S.Server.gm_koth_gain_points_interval,
				S.Server.gm_koth_gain_points_interval);
		L.info("Team " + occupyingTeam.getName() + " occupied the base!");
	}

	@Override
	public Team determineProgressingTeam(Base base, GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects) {
		if (presentObjects.size() == 1) {
			Unit onlyUnitInside = (Unit) presentObjects.iterator().next();
			return onlyUnitInside.getTeam();
		}
		return null;
	}

	class NeutralBasePointsAdder extends TimerTask {

		private final Player baseOwner;

		public NeutralBasePointsAdder(Player baseOwner) {
			this.baseOwner = baseOwner;
		}

		@Override
		public void run() {
			gameInfo.getEventTriggerer().changeStatOfPlayerByAmount(
					StatsProperty.KILLS, baseOwner,
					S.Server.gm_koth_points_per_interval);
		}

	}

	@Override
	public void onBaseLost(Base base, Team losingTeam) {
		Timer timerForBase = neutralBasePointsAdderTimer.get(base);
		L.info("Team " + losingTeam.getName() + " lost the base with id "
				+ base.getId() + "!");
		if (timerForBase == null) {
			L.severe("NeutralBasePointsAdder is null when base is lost!");
			return;
		}

		timerForBase.cancel();
		neutralBasePointsAdderTimer.put(base, null);
	}
}
