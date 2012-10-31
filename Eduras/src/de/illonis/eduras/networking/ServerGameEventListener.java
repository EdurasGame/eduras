/**
 * 
 */
package de.illonis.eduras.networking;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 *
 */
public class ServerGameEventListener implements GameEventListener {
	
	private Buffer outputBuffer;

	/* (non-Javadoc)
	 * @see de.illonis.eduras.interfaces.GameEventListener#onNewObjectPosition(de.illonis.eduras.GameObject)
	 */
	@Override
	public void onNewObjectPosition(GameObject o) {
		
		MovementEvent moveEvent;
		String msg = null;
		try {
			moveEvent = new MovementEvent(GameEventNumber.SET_POS, o.getId());
			NetworkMessageSerializer.serialize(moveEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		outputBuffer.append(msg);

	}

}
