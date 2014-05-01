package de.illonis.eduras.gamemodes;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameobjects.NeutralBase;
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

	private HashMap<NeutralBase, Timer> neutralBasePointsAdderTimer;

	/**
	 * Create a new KingOfTheHill mode.
	 * 
	 * @param gameInfo
	 *            gameinfo to work with
	 */
	public KingOfTheHill(GameInformation gameInfo) {
		super(gameInfo);

		neutralBasePointsAdderTimer = new HashMap<NeutralBase, Timer>();
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
	public void onBaseOccupied(NeutralBase base, Team occupyingTeam) {
		Player player = occupyingTeam.getPlayers().getFirst();
		Timer timerForBase = new Timer();
		neutralBasePointsAdderTimer.put(base, timerForBase);
		timerForBase.schedule(
				new NeutralBasePointsAdder(player.getPlayerMainFigure()),
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
	public void onBaseLost(NeutralBase base, Team losingTeam) {
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
