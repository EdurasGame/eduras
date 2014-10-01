package de.illonis.eduras.gameclient;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
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

	private final ChatCache chatCache;
	private final TreeSet<Integer> selectedUnits;
	private final VisionInformation visionInfo;
	private int currentItemSelected;
	private Player currentResurrectTarget;
	private ObjectType typeOfItemToSpawn;
	private int currentActionSelected;
	private GameEventNumber currentSpellSelected;

	/**
	 * Creates a new storage.
	 */
	public ClientData() {
		currentItemSelected = -1;
		currentActionSelected = -1;
		chatCache = new ChatCache();
		selectedUnits = new TreeSet<Integer>();
		visionInfo = new VisionInformation();
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
	 * Returns the currently selected action slot number.
	 * 
	 * @return slotnumber
	 */
	public int getCurrentActionSelected() {
		return currentActionSelected;
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
	 * Sets the currently selected action slot number.
	 * 
	 * @param currentActionSelected
	 */
	public void setCurrentActionSelected(int currentActionSelected) {
		this.currentActionSelected = currentActionSelected;
	}

	/**
	 * @return vision information.
	 */
	public VisionInformation getVisionInfo() {
		return visionInfo;
	}

	/**
	 * Store the player to be resurrected.
	 * 
	 * @param currentResurrectTarget
	 */
	public void setCurrentResurrectTarget(Player currentResurrectTarget) {
		this.currentResurrectTarget = currentResurrectTarget;
	}

	/**
	 * Determines which player is currently selected to be resurrected.
	 * 
	 * @return player
	 */
	public Player getCurrentResurrectTarget() {
		return currentResurrectTarget;
	}

	/**
	 * Store the type of item to be spawned next.
	 * 
	 * @param typeOfItemToSpawn
	 */
	public void setCurrentItemSpawnType(ObjectType typeOfItemToSpawn) {
		this.typeOfItemToSpawn = typeOfItemToSpawn;
	}

	/**
	 * Determines which type of item is currently selected to be spawned.
	 * 
	 * @return type
	 */
	public ObjectType getTypeOfItemToSpawn() {
		return typeOfItemToSpawn;
	}

	/**
	 * @return the chat cache.
	 */
	public ChatCache getChatCache() {
		return chatCache;
	}

	public GameEventNumber getCurrentSpellSelected() {
		return currentSpellSelected;
	}

	public void setCurrentSpellSelected(GameEventNumber spell) {
		currentSpellSelected = spell;
	}

}