/**
 * 
 */
package de.illonis.eduras.interfaces;

import java.util.ArrayList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.events.GameEvent;

/**
 * Interface for a game logic. A class handling the game logic should implement
 * the here declared methods.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface GameLogicInterface {

	public void onGameEventAppeared(GameEvent event);

	public GameInformation getGame();

	public void addGameEventListener(GameEventListener listener);

	public ArrayList<GameEventListener> getListenerList();

	public void onShutdown();

	public ObjectFactory getObjectFactory();
}
