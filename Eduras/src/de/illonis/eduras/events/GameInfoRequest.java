/**
 * 
 */
package de.illonis.eduras.events;

/**
 * This class represents a request from a client to the server for all the
 * available game information.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class GameInfoRequest extends GameEvent {
	
	private final int requester;
	
	/**
	 * Creates a new GameInfoRequest.
	 * @param requester The id of the client which requests the info.
	 */
	public GameInfoRequest(int requester) {
		super(GameEventNumber.INFORMATION_REQUEST);
		this.requester = requester;
	}

	/**
	 * Returns the requester.
	 * @return The id of the client which requests the information.
	 */
	public int getRequester() {
		return requester;
	}
}
