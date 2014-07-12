package de.illonis.eduras.gameclient.gui.animation;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory.EffectNumber;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.weapons.RocketMissile;
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

	private final InformationProvider infos;

	/**
	 * Creates a new effect handler.
	 */
	public ClientEffectHandler() {
		infos = EdurasInitializer.getInstance().getInformationProvider();
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o = infos.findObjectById(event.getId());
		if (o instanceof RocketMissile) {
			EffectFactory.createEffectAt(EffectNumber.ROCKET,
					o.getPositionVector());
		}
	}
}
