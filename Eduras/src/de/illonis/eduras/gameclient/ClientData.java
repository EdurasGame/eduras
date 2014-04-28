package de.illonis.eduras.gameclient;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import de.illonis.eduras.gameclient.gui.animation.Animation;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Holds data that are stored only on client side and do not have to be
 * transferred to server and thus are not retrievable by
 * {@link InformationProvider}.
 * 
 * @author illonis
 * 
 */
public final class ClientData {
	private final TreeSet<Integer> selectedUnits;
	private final LinkedList<Animation> animations;
	private final VisionInformation visionInfo;
	private int currentItemSelected;
	private PlayerMainFigure currentResurrectTarget;

	/**
	 * Creates a new storage.
	 */
	public ClientData() {
		currentItemSelected = -1;
		selectedUnits = new TreeSet<Integer>();
		animations = new LinkedList<Animation>();
		visionInfo = new VisionInformation();
	}

	/**
	 * Resets all stored data.
	 */
	public void reset() {
		clearSelectedUnits();
		clearAnimations();
	}

	/**
	 * Removes selection.
	 */
	public void clearSelectedUnits() {
		selectedUnits.clear();
	}

	/**
	 * Set the selected units.
	 * 
	 * @param unitIds
	 *            a list of ids of selected units.
	 */
	public void setSelectedUnits(LinkedList<Integer> unitIds) {
		clearSelectedUnits();
		selectedUnits.addAll(unitIds);
	}

	/**
	 * Returns ids of all selected units.
	 * 
	 * @return a list of selected units.
	 */
	public Set<Integer> getSelectedUnits() {
		return selectedUnits;
	}

	/**
	 * Marks a single unit as selected.
	 * 
	 * @param unitId
	 *            the id of the selected unit.
	 */
	public void setSelectedUnit(int unitId) {
		clearSelectedUnits();
		selectedUnits.add(unitId);
	}

	/**
	 * Returns the currently selected item's slot number.
	 * 
	 * @return slotnumber
	 */
	public int getCurrentItemSelected() {
		return currentItemSelected;
	}

	/**
	 * Sets the currently selected item's slot number.
	 * 
	 * @param currentItemSelected
	 */
	public void setCurrentItemSelected(int currentItemSelected) {
		this.currentItemSelected = currentItemSelected;
	}

	/**
	 * Adds an animation to data so it will be rendered when running.
	 * 
	 * @param anim
	 *            the animation.
	 */
	public void addAnimation(Animation anim) {
		animations.add(anim);
	}

	/**
	 * @return the list of active animations.
	 */
	public LinkedList<Animation> getAnimations() {
		return animations;
	}

	/**
	 * Removes an ended animation.
	 * 
	 * @param animation
	 *            the animation to remove.
	 */
	public void removeAnimation(Animation animation) {
		animations.remove(animation);
	}

	/**
	 * Removes all animations.
	 */
	public void clearAnimations() {
		animations.clear();
	}

	/**
	 * @return vision information.
	 */
	public VisionInformation getVisionInfo() {
		return visionInfo;
	}

	public void setCurrentResurrectTarget(
			PlayerMainFigure currentResurrectTarget) {
		this.currentResurrectTarget = currentResurrectTarget;
	}

	public PlayerMainFigure getCurrentResurrectTarget() {
		return currentResurrectTarget;
	}

}
