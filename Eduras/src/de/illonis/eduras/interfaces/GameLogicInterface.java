package de.illonis.eduras.interfaces;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.logic.LogicGameWorker;

/**
 * Interface for a game logic. A class handling the game logic should implement
 * the here declared methods.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface GameLogicInterface {

	/**
	 * Handles an incoming event. See {@link GameEventNumber} for detailed
	 * description.
	 * 
	 * @param event
	 *            event to handle.
	 */
	public void onGameEventAppeared(GameEvent event);

	/**
	 * Returns game information.
	 * 
	 * @return game information.
	 */
	public GameInformation getGame();

	/**
	 * Registers given listener for world-change events. Note that only one
	 * listener can be registered at once.
	 * 
	 * @param listener
	 *            Listener to register.
	 */
	public void setGameEventListener(GameEventListener listener);

	/**
	 * Returns the listener.
	 * 
	 * @return listener.
	 */
	public GameEventListener getListener();

	/**
	 * Starts the {@link LogicGameWorker}.
	 * 
	 * @return the worker.
	 */
	public LogicGameWorker startWorker();

	/**
	 * Indicates shutdown of server/client and stops the gameworker.
	 * 
	 * @author illonis
	 */
	public void stopWorker();

	/**
	 * Returns object factory.
	 * 
	 * @return object factory.
	 */
	public ObjectFactory getObjectFactory();

	public LogicGameWorker getWorker();

}
