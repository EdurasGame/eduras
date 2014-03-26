package de.illonis.eduras.gamemodes;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A game mode like deathmatch, but the user gets extra points when he conquers
 * neutralbases.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class KingOfTheHill extends Deathmatch {

	private Timer neutralBasePointsAdderTimer;

	/**
	 * Create a new KingOfTheHill mode.
	 * 
	 * @param gameInfo
	 *            gameinfo to work with
	 */
	public KingOfTheHill(GameInformation gameInfo) {
		super(gameInfo);

		neutralBasePointsAdderTimer = null;
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
	public void onBaseOccupied(Team occupyingTeam) {
		PlayerMainFigure player = occupyingTeam.getPlayers().getFirst();
		neutralBasePointsAdderTimer = new Timer();
		neutralBasePointsAdderTimer.schedule(
				new NeutralBasePointsAdder(player),
				S.gm_koth_gain_points_interval, S.gm_koth_gain_points_interval);
		L.info("Team " + occupyingTeam.getName() + " occupied the base!");
	}

	class NeutralBasePointsAdder extends TimerTask {

		private final PlayerMainFigure baseOwner;

		public NeutralBasePointsAdder(PlayerMainFigure baseOwner) {
			this.baseOwner = baseOwner;
		}

		@Override
		public void run() {
			gameInfo.getEventTriggerer().changeStatOfPlayerByAmount(
					StatsProperty.KILLS, baseOwner,
					S.gm_koth_points_per_interval);
		}

	}

	@Override
	public void onBaseLost(Team losingTeam) {
		L.info("Team " + losingTeam.getName() + " lost the base!");
		if (neutralBasePointsAdderTimer == null) {
			L.severe("NeutralBasePointsAdder is null when base is lost!");
			return;
		}

		neutralBasePointsAdderTimer.cancel();
		neutralBasePointsAdderTimer = null;
	}
}
