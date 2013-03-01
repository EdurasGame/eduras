package de.illonis.eduras.logic;


/**
 * Triggers events from console.
 * 
 * @author illonis
 * 
 */
public class ConsoleEventTriggerer {

	private ServerEventTriggerer triggerer;

	public ConsoleEventTriggerer(ServerEventTriggerer serverTriggerer) {
		this.triggerer = serverTriggerer;
	}

}
