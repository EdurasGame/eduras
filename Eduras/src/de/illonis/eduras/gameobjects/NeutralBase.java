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

	public NeutralBase(GameInformation game, int id, int mult) {
		super(game, id);
		currentOwnerTeam = null;
		currentProgressingTeam = null;
		resourceGenerateAmount = S.neutralbase_resource_baseamount * mult;
		resourceGenerateTimeInterval = S.neutralbase_resource_interval;
		// TODO: register at serverlogicgameworker
	}

	public int getProgress() {
		return progress;
	}

	public Team getCurrentOwnerTeam() {
		return currentOwnerTeam;
	}

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
