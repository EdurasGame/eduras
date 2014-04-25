package de.illonis.eduras.events;

public class RTSActionEvent extends GameEvent {

	private int executingPlayer;

	public RTSActionEvent(GameEventNumber type, int executingPlayer) {
		super(type);

		this.executingPlayer = executingPlayer;
		putArgument(executingPlayer);
	}

	public int getExecutingPlayer() {
		return executingPlayer;
	}

}
