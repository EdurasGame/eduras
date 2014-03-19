package de.illonis.eduras.gameobjects;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Rectangle;
import de.illonis.eduras.units.Unit;

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
		setObjectType(ObjectType.NEUTRAL_BASE);
		try {
			setShape(new Rectangle(new Vector2D(-20, 20), new Vector2D(20, -20)));
		} catch (ShapeVerticesNotApplicableException e) {
			L.log(Level.WARNING, "TODO: message", e);
		}
		currentOwnerTeam = null;
		currentProgressingTeam = null;
		resourceGenerateAmount = S.neutralbase_resource_baseamount * mult;
		resourceGenerateTimeInterval = S.neutralbase_resource_interval;
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
		if (object.isUnit()) {
			Unit u = (Unit) object;
			Team team = u.getTeam();
			if (team == null)
				return;

			if (!team.equals(currentProgressingTeam)) {
				// getGame().getEventTriggerer().onBaseStartCapturing(this,
				// team);
				L.log(Level.INFO, "Team: " + team.getName()
						+ " starts taking over the base!");
				currentProgressingTeam = team;
			}
		}
	}

	@Override
	public void onObjectLeft(GameObject object) {
		if (object.isUnit()) {
			Unit u = (Unit) object;
			Team team = u.getTeam();
			if (team == null)
				return;

			if (team.equals(currentProgressingTeam)) {
				// getGame().getEventTriggerer().onBaseStartCapturing(this,
				// team);
				L.log(Level.INFO, "Team: " + team.getName() + " left the base!");
				currentProgressingTeam = null;
			}
		}
	}

	@Override
	protected void intervalElapsed(long delta) {
		// System.out.println("[BASE] elapsed");
	}

}
