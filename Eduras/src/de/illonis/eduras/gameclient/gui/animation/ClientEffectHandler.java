package de.illonis.eduras.gameclient.gui.animation;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.particles.ConfigurableEmitter;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ItemUseFailedEvent;
import de.illonis.eduras.events.ItemUseFailedEvent.Reason;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;
import de.illonis.eduras.gameobjects.GameObject;
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

	/**
	 * Creates a new effect handler.
	 */
	public ClientEffectHandler() {
		infos = EdurasInitializer.getInstance().getInformationProvider();
		objectEffects = new HashMap<Integer, ConfigurableEmitter>();
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		if (event.getOwner() == infos.getOwnerID() && event.getObjectId() != -1) {
			// TODO: Change sound to a new one.
			SoundMachine.play(SoundType.CLICK);
		}
	}

	@Override
	public void onHealthChanged(Unit unit, int oldValue, int newValue) {
		try {
			if (unit.equals(infos.getPlayer().getPlayerMainFigure())) {
				if (newValue > oldValue) {
					// TODO: Change the sounds here
					SoundMachine.play(SoundType.CLICK);
				} else {
					if (newValue < oldValue) {
						SoundMachine.play(SoundType.ERROR);
					}
				}
			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find player main figure.", e);
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
		if (o.getType().equals(ObjectType.ROCKET_MISSILE)) {
			EffectFactory.createEffectAt(EffectNumber.ROCKET,
					o.getPositionVector());
		}
	}

	@Override
	public void onItemUseFailed(ItemUseFailedEvent itemFailedEvent) {
		if (itemFailedEvent.getReason() == Reason.AMMO_EMPTY) {
			SoundMachine.play(SoundType.AMMO_EMPTY, 1f, .7f);
		}
	}
}
