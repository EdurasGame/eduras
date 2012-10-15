package de.illonis.eduras;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;

/**
 * A first (dummy) implementation of game logic.
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 *
 */
public class Logic implements GameLogicInterface {
	
	Game currentGame;

	public Logic(Game g) {
		
		this.currentGame = g;
		
	}
	
	/**
	 * Handles an incoming event. See {@link GameEventNumber} for detailed description.
	 * @param event 
	 */
	@Override
	public synchronized void onGameEventAppeared(GameEvent event) {
		
		
		if(event instanceof MovementEvent) {
			
			int yPos;
			int xPos;
			
			MovementEvent moveEvent = (MovementEvent) event;
			
			switch(moveEvent.getType()) {
			
			case MOVE_DOWN:
				yPos = currentGame.getPlayer1().getYPosition();
				currentGame.getPlayer1().setYPosition(--yPos);
				break;
			case MOVE_LEFT:
				xPos = currentGame.getPlayer1().getXPosition();
				currentGame.getPlayer1().setXPosition(--xPos);
				break;
			case MOVE_RIGHT:
				xPos = currentGame.getPlayer1().getXPosition();
				currentGame.getPlayer1().setXPosition(++xPos);
				break;
			case MOVE_UP:
				yPos = currentGame.getPlayer1().getYPosition();
				currentGame.getPlayer1().setYPosition(++yPos);
				break;
			default:
				break;
			}
		}
	}

}
