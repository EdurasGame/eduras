package de.illonis.eduras.gameclient.gui.animation;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.particles.ConfigurableEmitter;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.AoEDamageEvent;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.ItemUseFailedEvent.Reason;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.Unit;

/**
 * Renders effects on client based on events.
 * 
 * @author illonis
 * 
 */
public class ClientEffectHandler extends GameEventAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(ClientEffectHandler.class.getName());

	private final HashMap<Integer, ConfigurableEmitter> objectEffects;

	private final InformationProvider infos;

	private boolean playSounds;

	/**
	 * Creates a new effect handler.
	 */
	public ClientEffectHandler() {
		playSounds = false;
		infos = EdurasInitializer.getInstance().getInformationProvider();
		objectEffects = new HashMap<Integer, ConfigurableEmitter>();
	}

	@Override
	public void onHealthChanged(Unit unit, int oldValue, int newValue) {
		if (playSounds) {
			try {
				if (unit.equals(infos.getPlayer().getPlayerMainFigure())) {
					if (newValue > oldValue) {
						SoundMachine.play(SoundType.LOOT);
					} else {
						if (newValue < oldValue) {
							SoundMachine.play(SoundType.HURT);
						}
					}
				}
			} catch (ObjectNotFoundException e) {
				L.log(Level.WARNING, "Cannot find player main figure.", e);
			}
		}
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o;
		try {
			o = infos.findObjectById(event.getId());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object to remove!", e);
			return;
		}
	}

	@Override
	public void onItemUseFailed(ItemUseFailedEvent itemFailedEvent) {
		if (itemFailedEvent.getReason() == Reason.AMMO_EMPTY) {
			SoundMachine.play(SoundType.AMMO_EMPTY, 1f, .7f);
		}
	}

	@Override
	public void onBaseConquered(Base base, Team conqueringTeam) {
		if (playSounds) {
			SoundMachine.play(SoundType.TADA);
		}
	}

	@Override
	public void onGameReady() {
		playSounds = true;
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		playSounds = false;
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {

		if (playSounds) {
			try {
				Player player = infos.getPlayer();
				if (event.getOwner() == player.getPlayerId()) {
					player.getInventory().getItemBySlot(event.getItemSlot());
					SoundMachine.play(SoundType.LOOT, 1f, .8f);
				}
			} catch (ItemSlotIsEmptyException | ObjectNotFoundException e) {
				// item lost or something like that
			}
		}
	}

	@Override
	public void onStartRound() {
		playSounds = true;
	}

	@Override
	public void onAoEDamage(AoEDamageEvent event) {
		switch (event.getObjectType()) {
		case ROCKET_MISSILE:
		case MISSILE_SPLASH:
			EffectFactory.createEffectAt(EffectNumber.ROCKET,
					event.getPosition());
			break;
		default:
			L.warning("Cannot handle aoe effect for type "
					+ event.getObjectType());
		}
	}
}
