package de.illonis.eduras.gameobjects;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.settings.S;

/**
 * A neutral base that can be captured.
 * 
 * @author illonis
 * 
 */
public class NeutralBase extends TriggerArea {

	private final static Logger L = EduLog.getLoggerFor(NeutralBase.class
			.getName());

	private Team currentOwnerTeam;
	private Team currentProgressingTeam;
	private int progress = 0;

	private long resourceGenerateTimeInterval;

	private int resourceGenerateAmount;

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
		currentOwnerTeam = null;
		currentProgressingTeam = null;
		resourceGenerateAmount = S.neutralbase_resource_baseamount * mult;
		resourceGenerateTimeInterval = S.neutralbase_resource_interval;
		// TODO: register at serverlogicgameworker
	}

	/**
	 * @return the progress of the current progressing team [0,100]
	 * 
	 * @see #getCurrentProgressingTeam()
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @return the id of the current owning team.
	 */
	public Team getCurrentOwnerTeam() {
		return currentOwnerTeam;
	}

	/**
	 * @return the team that currently gets progress in claiming this base.
	 */
	public Team getCurrentProgressingTeam() {
		return currentProgressingTeam;
	}

	@Override
	public void onObjectEntered(GameObject object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onObjectLeft(GameObject object) {
		// TODO Auto-generated method stub

	}

}
