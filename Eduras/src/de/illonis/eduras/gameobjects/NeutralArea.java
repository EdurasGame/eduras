package de.illonis.eduras.gameobjects;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Rectangle;
import de.illonis.eduras.units.Unit;

/**
 * A neutral area that can be captured. When a team enters the area it can
 * eventually take over the base (depending on the game mode). Once it's taken
 * over, the onNeutralAreaOccupied method is called, so subclasses should
 * overwrite it.
 * 
 * @author illonis
 * 
 */
public abstract class NeutralArea extends TriggerArea {

	private final static Logger L = EduLog.getLoggerFor(NeutralArea.class
			.getName());

	private Team currentOwnerTeam;
	private Team currentProgressingTeam;
	private double progress = 0;
	private long timeNeededForTakeOver = -1;

	/**
	 * Creates a new NeutralArea.
	 * 
	 * @param game
	 *            the gameinformation context
	 * @param timingSource
	 * @param id
	 */
	public NeutralArea(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.NEUTRAL_BASE);
		try {
			setShape(new Rectangle(new Vector2D(-20, 20), new Vector2D(20, -20)));
		} catch (ShapeVerticesNotApplicableException e) {
			L.log(Level.WARNING, "TODO: message", e);
		}
		currentOwnerTeam = null;
		currentProgressingTeam = null;
	}

	/**
	 * @return the progress of the current progressing team [0,100]
	 * 
	 * @see #getCurrentProgressingTeam()
	 */
	public double getProgress() {
		return progress;
	}

	/**
	 * @return the id of the current owning team.
	 */
	public Team getCurrentOwnerTeam() {
		return currentOwnerTeam;
	}

	/**
	 * Sets the time a team needs to take over the area. If set to a negative
	 * value or 0, it will be taken over instantly.
	 * 
	 * @param timeNeeded
	 *            Time in milliseconds.
	 */
	public void setTimeNeeded(long timeNeeded) {
		this.timeNeededForTakeOver = timeNeeded;
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

			// getGame().getEventTriggerer().onBaseStartCapturing(this,
			// team);
			determineAndSetCurrentProgressingTeam(object, true);
			L.log(Level.INFO, "Team: " + currentProgressingTeam.getName()
					+ " is currently taking over the neutral area!");
		}
	}

	private void determineAndSetCurrentProgressingTeam(GameObject object,
			boolean entered) {
		Team newTeam = determineProgressingTeam(object, entered);
		if (newTeam == null || !newTeam.equals(currentProgressingTeam)) {
			progress = 0;
			currentProgressingTeam = newTeam;
		}
	}

	protected abstract Team determineProgressingTeam(GameObject object,
			boolean objectEntered);

	@Override
	public void onObjectLeft(GameObject object) {
		if (object.isUnit()) {
			Unit u = (Unit) object;
			Team team = u.getTeam();
			if (team == null)
				return;

			// getGame().getEventTriggerer().onBaseStartCapturing(this,
			// team);
			determineAndSetCurrentProgressingTeam(object, false);
			L.log(Level.INFO, "Team: " + team.getName() + " left the base!");
		}
	}

	@Override
	protected void intervalElapsed(long delta) {
		if (currentProgressingTeam != null && progress < 100) {
			if (timeNeededForTakeOver <= 0) {
				progress = 100;
			} else {
				double newProgress = (((double) delta / (double) timeNeededForTakeOver) * 100);
				progress += newProgress;
			}
			if (progress >= 100) {
				progress = 100;

				if (currentOwnerTeam != null) {
					onNeutralAreaLost(currentOwnerTeam);
				}
				currentOwnerTeam = currentProgressingTeam;
				onNeutralAreaOccupied(currentProgressingTeam);
			}
		}
	}

	protected abstract void onNeutralAreaOccupied(Team occupyingTeam);

	protected abstract void onNeutralAreaLost(Team losingTeam);

}