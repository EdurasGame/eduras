package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.logic.EventTriggerer;

public abstract class RTSAction {

	protected int costs;
	protected Player executingPlayer;

	private final static Logger L = EduLog.getLoggerFor(RTSAction.class
			.getName());

	public RTSAction(Player executingPlayer, int costs) {
		this.costs = costs;
		this.executingPlayer = executingPlayer;
	}

	public void execute(GameInformation info) {
		Team executingTeam = executingPlayer.getTeam();

		if (executingTeam.getResource() >= costs) {

			EventTriggerer eventTriggerer = info.getEventTriggerer();
			eventTriggerer.changeResourcesOfTeamByAmount(executingTeam, -costs);
			executeAction(info);
		} else {
			// should be caught on client already
			L.warning("Received an RTSAction although the executing team doesn't have sufficient resources.");
		}
	}

	protected abstract void executeAction(GameInformation info);

	public int getCosts() {
		return costs;
	}

	public Player getExecutingPlayer() {
		return executingPlayer;
	}
}
