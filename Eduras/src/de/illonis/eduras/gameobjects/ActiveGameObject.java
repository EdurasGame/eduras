package de.illonis.eduras.gameobjects;

import de.illonis.eduras.GameInformation;

public abstract class ActiveGameObject extends GameObject {

	public ActiveGameObject(GameInformation game, int id) {
		super(game, id);
	}

	@Override
	public abstract void onCollision(GameObject collidingObject);

	public abstract ArtificialIntelligence getAI();

}
