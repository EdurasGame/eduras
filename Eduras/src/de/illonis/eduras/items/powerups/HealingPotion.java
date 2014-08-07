package de.illonis.eduras.items.powerups;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.PowerUpItem;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

public class HealingPotion extends PowerUpItem {

	private final static Logger L = EduLog.getLoggerFor(HealingPotion.class
			.getName());

	private int healAmount;

	public HealingPotion(TimingSource timingSource, GameInformation gi, int id) {
		super(ObjectType.HEALING_POTION, timingSource, gi, id);
		setShape(new Circle(getXPosition(), getYPosition(),
				S.Server.go_healingpotion_radius_size));
		setHealAmount(S.Server.go_healingpotion_healamount);
	}

	@Override
	public void onActivation(PlayerMainFigure touchingPlayer) {
		getGame().getEventTriggerer().changeHealthByAmount(touchingPlayer,
				getHealAmount());
	}

	/**
	 * Returns the amount of healthpoints by which a player who touches this
	 * item is healed.
	 * 
	 * @return heal amount
	 */
	public int getHealAmount() {
		return healAmount;
	}

	/**
	 * Sets the amount of healthpoints by which a player who touches this item
	 * is healed.
	 * 
	 * @param healAmount
	 */
	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}
}
