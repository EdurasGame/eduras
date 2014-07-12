package de.illonis.eduras.gameclient.gui.animation;

import java.util.HashMap;
import java.util.logging.Logger;

import org.newdawn.slick.particles.ConfigurableEmitter;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;

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
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o = infos.findObjectById(event.getId());
		if (o.getType().equals(ObjectType.ROCKET_MISSILE)) {
			EffectFactory.createEffectAt(EffectNumber.ROCKET,
					o.getPositionVector());
		}
	}
}
