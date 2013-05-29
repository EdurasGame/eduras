package de.illonis.eduras.events;

public class DeathEvent extends GameEvent {
	int killedBy;
	int dead;

	public DeathEvent(int dead, int killedBy) {
		super(GameEventNumber.DEATH);
		this.dead = dead;
		this.killedBy = killedBy;
	}

	public int getKilledBy() {
		return killedBy;
	}

	public void setKilledBy(int killedBy) {
		this.killedBy = killedBy;
	}

	public int getDead() {
		return dead;
	}

	public void setDead(int dead) {
		this.dead = dead;
	}

}
