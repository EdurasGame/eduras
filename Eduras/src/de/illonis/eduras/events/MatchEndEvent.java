package de.illonis.eduras.events;

/**
 * This event tells that there's a winner of the match given the winner's id.
 * The shall be interpreted in dependency of the game mode, so it could be a
 * teamnumber or a playernumber or something else.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MatchEndEvent extends GameEvent {

	int winnerId = -1;

	/**
	 * Creates a MatchEndEvent telling that the party with the id 'winnerId' has
	 * won.
	 * 
	 * @param winnerId
	 *            The id of the winner. -1 if there is no winner.
	 */
	public MatchEndEvent(int winnerId) {
		super(GameEventNumber.MATCH_END);
		this.winnerId = winnerId;
		putArgument(winnerId);
	}

	/**
	 * Returns the winner's id.
	 * 
	 * @return The id of the winner.
	 */
	public int getWinnerId() {
		return winnerId;
	}

}
