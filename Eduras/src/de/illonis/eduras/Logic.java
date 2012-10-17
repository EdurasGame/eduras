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
			
			GameObject player = currentGame.getPlayer1();
			
			switch(moveEvent.getType()) {
			
			case MOVE_DOWN:
				yPos = player.getYPosition();
				player.setYPosition(--yPos);
				break;
			case MOVE_LEFT:
				xPos = player.getXPosition();
				player.setXPosition(--xPos);
				break;
			case MOVE_RIGHT:
				xPos = player.getXPosition();
				player.setXPosition(++xPos);
				break;
			case MOVE_UP:
				yPos = player.getYPosition();
				player.setYPosition(++yPos);
				break;
			case MOVE_POS:
				int newXPos = moveEvent.getNewXPos();
				int newYPos = moveEvent.getNewYPos();
				player.setYPosition(newYPos);
				player.setXPosition(newXPos);
			default:
				break;
			}
		}
	}

}
