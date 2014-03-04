package de.illonis.eduras.gameclient.gui;

import de.illonis.eduras.utils.TimedTasksHolder;

/**
 * Holds all the TimedTasks which are client related an shall be started when
 * the game starts rendering.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class TimedTasksHolderGUI extends TimedTasksHolder {

	private static TimedTasksHolderGUI instance;

	/**
	 * Returns the singleton instance of {@link TimedTasksHolderGUI}.
	 * 
	 * @return The singleton instance.
	 */
	public static TimedTasksHolderGUI getInstance() {
		if (instance == null) {
			instance = new TimedTasksHolderGUI();
		}
		return instance;
	}

	private TimedTasksHolderGUI() {
		super();
	}

}
