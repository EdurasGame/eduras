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
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;
import de.illonis.eduras.gameobjects.Base;
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
						SoundMachine.play(SoundType.SPELL_HEAL);
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
	public void onRespawn(RespawnEvent event) {
		if (playSounds && event.getOwner() == infos.getOwnerID()) {
			SoundMachine.play(SoundType.SPELL_REZZ);
		}
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
	}

	@Override
	public void onItemUseFailed(ItemUseFailedEvent itemFailedEvent) {
		if (itemFailedEvent.getReason() == Reason.AMMO_EMPTY) {
			SoundMachine.play(SoundType.AMMO_EMPTY);
		}
	}

	@Override
	public void onBaseConquered(Base base, Team conqueringTeam) {
		if (playSounds) {
			try {
				Team myTeam = infos.getPlayer().getTeam();
				if (conqueringTeam.equals(myTeam)) {
					SoundMachine.play(SoundType.BASE_CONQUERED);
				} else {
					SoundMachine.play(SoundType.BASE_LOST);
				}
			} catch (PlayerHasNoTeamException | ObjectNotFoundException e) {
				L.log(Level.WARNING,
						"No player found when playing base conquered sound.", e);
			}

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
					player.getInventory().getItemAt(event.getItemSlot());
					SoundMachine.play(SoundType.LOOT);
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
		case MINE_MISSILE:
			EffectFactory.createEffectAt(EffectNumber.ROCKET,
					event.getPosition());
			break;
		default:
			L.warning("Cannot handle aoe effect for type "
					+ event.getObjectType());
		}
	}
}
