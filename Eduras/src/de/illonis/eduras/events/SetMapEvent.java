package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Notification that the map has changed.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetMapEvent extends GameEvent {

	private final static Logger L = EduLog.getLoggerFor(SetMapEvent.class
			.getName());

	private String nameOfNewMap;
	private String hashOfMap;

	/**
	 * Create the event.
	 * 
	 * @param nameOfNewMap
	 *            name of map the game changed to
	 * @param hashOfMap
	 *            hash to check the version
	 */
	public SetMapEvent(String nameOfNewMap, String hashOfMap) {
		super(GameEventNumber.SET_MAP);
		this.nameOfNewMap = nameOfNewMap;
		this.hashOfMap = hashOfMap;
		putArgument(nameOfNewMap);
		putArgument(hashOfMap);
	}

	/**
	 * Returns name of the map.
	 * 
	 * @return mapname
	 */
	public String getNameOfNewMap() {
		return nameOfNewMap;
	}

	/**
	 * Returns the maps 256SHA hash value.
	 * 
	 * @return hash
	 */
	public String getHashOfMap() {
		return hashOfMap;
	}

}
