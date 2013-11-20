package de.illonis.eduras.gameclient;

import java.util.LinkedList;

import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Holds data that are stored only on client side and do not have to be
 * transferred to server and thus are not retrievable by
 * {@link InformationProvider}.
 * 
 * @author illonis
 * 
 */
public final class ClientData {
	private final LinkedList<Integer> selectedUnits;

	ClientData() {
		selectedUnits = new LinkedList<Integer>();
	}

	/**
	 * Resets all stored data.
	 */
	public void reset() {
		clearSelectedUnits();
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
	public LinkedList<Integer> getSelectedUnits() {
		return new LinkedList<Integer>(selectedUnits);
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

}
