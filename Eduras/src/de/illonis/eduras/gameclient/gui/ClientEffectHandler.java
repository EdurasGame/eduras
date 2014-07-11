package de.illonis.eduras.gameclient.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.weapons.RocketMissile;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Renders effects on client.
 * 
 * @author illonis
 * 
 */
public class ClientEffectHandler extends GameEventAdapter {
	private final static Logger L = EduLog
			.getLoggerFor(ClientEffectHandler.class.getName());
	private final InformationProvider infos;

	public ClientEffectHandler() {
		infos = EdurasInitializer.getInstance().getInformationProvider();
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o = infos.findObjectById(event.getId());
		if (o instanceof RocketMissile) {
			try {
				EffectFactory.createEffectAt(EffectNumber.ROCKET,
						o.getPositionVector());
			} catch (SlickException e) {
				L.log(Level.WARNING, "Could not create effect onremove", e);
			}
		}
	}
}
