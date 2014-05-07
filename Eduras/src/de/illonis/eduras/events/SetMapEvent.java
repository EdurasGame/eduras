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

	/**
	 * Create the event.
	 * 
	 * @param nameOfNewMap
	 *            name of map the game changed to
	 */
	public SetMapEvent(String nameOfNewMap) {
		super(GameEventNumber.SET_MAP);
		this.nameOfNewMap = nameOfNewMap;
		putArgument(nameOfNewMap);
	}

	/**
	 * Returns name of the map.
	 * 
	 * @return mapname
	 */
	public String getNameOfNewMap() {
		return nameOfNewMap;
	}

}
