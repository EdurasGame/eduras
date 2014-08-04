package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * This event sets a single setting property.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SetSettingPropertyEvent extends GameEvent {

	private final static Logger L = EduLog
			.getLoggerFor(SetSettingPropertyEvent.class.getName());

	private final String settingName;
	private final String settingValue;

	public SetSettingPropertyEvent(String settingName, String settingValue) {
		super(GameEventNumber.SET_SETTINGPROPERTY);

		this.settingName = settingName;
		this.settingValue = settingValue;

		putArgument(settingName);
		putArgument(settingValue);
	}

	public String getSettingName() {
		return settingName;
	}

	public String getSettingValue() {
		return settingValue;
	}

}
