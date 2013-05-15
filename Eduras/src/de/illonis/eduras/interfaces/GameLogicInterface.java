package de.illonis.eduras.interfaces;

import java.util.ArrayList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;

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
	 * Registers given listener for world-change events.
	 * 
	 * @param listener
	 *            Listener to register.
	 */
	public void addGameEventListener(GameEventListener listener);

	/**
	 * Unregisters given listener for world-change events.
	 * 
	 * @param listener
	 *            Listener to unregister.
	 */
	public void removeGameEventListener(GameEventListener listener);

	/**
	 * Returns all listeners as list.
	 * 
	 * @return listener list.
	 */
	public ArrayList<GameEventListener> getListenerList();

	/**
	 * Indicates shutdown of server/client.
	 * 
	 * @author illonis
	 */
	public void onShutdown();

	/**
	 * Returns object factory.
	 * 
	 * @return object factory.
	 */
	public ObjectFactory getObjectFactory();
}
