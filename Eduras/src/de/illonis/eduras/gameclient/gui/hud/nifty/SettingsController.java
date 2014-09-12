package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * Manages settings.
 * 
 * @author illonis
 * 
 */
public class SettingsController extends EdurasScreenController {

	private Label resolutionLabel;
	private ListBox<KeyBinding> box;
	private final KeyBindings bindings;
	private final Settings settings;
	private CheckBox chooseOnPressBox;
	private CheckBox continuousItemUsageBox;
	private CheckBox mouseWheelSwitchBox;
	private CheckBox fullscreenBox;
	private Slider soundVolumeSlider;
	private Slider musicVolumeSlider;
	private DropDown<DisplayMode> resolutionSelect;

	private final static String POPUP_NOTSUPPORTED = "resPopup";
	private final static String POPUP_APPLY = "applyPopup";

	SettingsController(GameControllerBridge game) {
		super(game);
		settings = EdurasInitializer.getInstance().getSettings();
		bindings = settings.getKeyBindings();
	}

	/**
	 * returns back to server list.
	 */
	public void back() {
		SoundMachine.play(SoundType.CLICK, 2f);
		game.enterState(2, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
		try {
			settings.save();
		} catch (IOException e) {
			System.out.println("Could not save settings.");
		}
	}

	/**
	 * Saves checkbox changes.
	 * 
	 * @param checkboxId
	 *            the changed checkbox.
	 * @param event
	 *            the event that contains information.
	 */
	@NiftyEventSubscriber(pattern = ".*Box")
	public void onCheckboxChanged(final String checkboxId,
			final CheckBoxStateChangedEvent event) {
		String setting = checkboxId.substring(0, checkboxId.length() - 3);
		if (!checkboxId.contains("windowedMode")) {
			settings.setBooleanOption(setting, event.getCheckBox().isChecked());
		}
	}

	/**
	 * Saves slider changes
	 * 
	 * @param id
	 * @param event
	 */
	@NiftyEventSubscriber(pattern = ".*VolumeSlider")
	public void onSliderChange(final String id, final SliderChangedEvent event) {
		if (id.contains("sound")) {
			settings.setFloatOption(Settings.SOUND_VOLUME, event.getValue());
			game.setSoundVolume(event.getValue());
			SoundMachine.play(SoundType.AMMO_EMPTY);
		} else if (id.contains("music")) {
			settings.setFloatOption(Settings.MUSIC_VOLUME, event.getValue());
			game.setMusicVolume(event.getValue());
			// TODO: play music ;)
		}
	}

	/**
	 * Resets all keybindings.
	 */
	public void resetAllKeyBindings() {
		for (KeyBinding binding : KeyBinding.values()) {
			bindings.resetToDefault(binding);
		}
		box.refresh();
	}

	/**
	 * Resets the selected key binding.
	 */
	public void resetKeyBinding() {
		List<KeyBinding> selection = box.getSelection();
		if (selection.size() == 1) {
			bindings.resetToDefault(selection.get(0));
			box.refresh();
		}
	}

	private void fillResolutionSelect(Screen screen) {

		resolutionSelect.clear();
		try {
			DisplayMode currentMode = Display.getDisplayMode();
			resolutionLabel.setText("Current resolution: "
					+ currentMode.toString());
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			Arrays.sort(modes, new Comparator<DisplayMode>() {
				@Override
				public int compare(DisplayMode a, DisplayMode b) {
					if (a.getWidth() > b.getWidth())
						return -1;
					else if (a.getWidth() < b.getWidth())
						return 1;
					else if (a.getHeight() > b.getHeight())
						return -1;
					else if (a.getHeight() < b.getHeight())
						return 1;
					else if (a.getFrequency() > b.getFrequency())
						return -1;
					else if (a.getFrequency() < b.getFrequency())
						return 1;
					else
						return 0;
				}
			});
			for (DisplayMode displayMode : modes) {
				resolutionSelect.addItem(displayMode);
			}
			resolutionSelect.selectItem(currentMode);
		} catch (LWJGLException e) {
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initScreen(Screen screen) {

		Label hintLabel = screen.findNiftyControl("hintLabel", Label.class);
		hintLabel.setText("Select a keybinding and press a key to bind it.");
		box = screen.findNiftyControl("#keyBindingsList", ListBox.class);
		chooseOnPressBox = screen.findNiftyControl("chooseOnPressBox",
				CheckBox.class);
		continuousItemUsageBox = screen.findNiftyControl(
				"continuousItemUsageBox", CheckBox.class);
		mouseWheelSwitchBox = screen.findNiftyControl("mouseWheelSwitchBox",
				CheckBox.class);

		for (KeyBinding binding : KeyBinding.values()) {
			box.addItem(binding);
		}
		soundVolumeSlider = screen.findNiftyControl("soundVolumeSlider",
				Slider.class);
		musicVolumeSlider = screen.findNiftyControl("musicVolumeSlider",
				Slider.class);
		fullscreenBox = screen.findNiftyControl("windowedModeBox",
				CheckBox.class);
		resolutionSelect = screen.findNiftyControl("resolutionSelect",
				DropDown.class);
		resolutionLabel = screen.findNiftyControl("currentResolution",
				Label.class);

		fillResolutionSelect(screen);
	}

	/**
	 * Changes screen resolution on select.
	 * 
	 * @param id
	 *            the select box.
	 * @param event
	 *            the event.
	 */
	@NiftyEventSubscriber(id = "resolutionSelect")
	public void onDropDownSelectionChanged(final String id,
			final DropDownSelectionChangedEvent<DisplayMode> event) {
		// handled by "apply"-button
	}

	/**
	 * Listens for new keys.
	 * 
	 * @param key
	 *            the key to be set.
	 */
	public void keyPressed(int key) {
		List<KeyBinding> selection = box.getSelection();
		if (selection.size() == 1) {
			bindings.setKeyBinding(selection.get(0), key);
			box.refresh();
		}
	}

	/**
	 * applies selected resolution.
	 */
	public void applyResolution() {
		DisplayMode selected = resolutionSelect.getSelection();
		if (fullscreenBox.isChecked() && !selected.isFullscreenCapable()) {
			// fullscreen resolution not supported
			nifty.createPopupWithId(POPUP_NOTSUPPORTED, POPUP_NOTSUPPORTED);
			nifty.showPopup(nifty.getCurrentScreen(), POPUP_NOTSUPPORTED, null);
			return;
		}
		settings.setIntOption(Settings.WIDTH, selected.getWidth());
		settings.setIntOption(Settings.HEIGHT, selected.getHeight());
		settings.setBooleanOption(Settings.WINDOWED, !fullscreenBox.isChecked());
		try {
			settings.save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (!fullscreenBox.isChecked()) {
			try {
				game.changeResolution(selected.getWidth(),
						selected.getHeight(), true);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		} else {
			nifty.createPopupWithId(POPUP_APPLY, POPUP_APPLY);
			nifty.showPopup(nifty.getCurrentScreen(), POPUP_APPLY, null);
		}
	}

	public void closeResPopup() {
		nifty.closePopup(POPUP_NOTSUPPORTED);
	}

	public void closeApplyPopup() {
		nifty.closePopup(POPUP_APPLY);
	}

	public void leaveState() {
		nifty.setIgnoreKeyboardEvents(false);
	}

	public void enterState() {
		nifty.setIgnoreKeyboardEvents(true);
		chooseOnPressBox.setChecked(settings
				.getBooleanSetting(Settings.CHOOSE_ON_PRESS));
		continuousItemUsageBox.setChecked(settings
				.getBooleanSetting(Settings.CONTINUOUS_ITEM_USAGE));
		mouseWheelSwitchBox.setChecked(settings
				.getBooleanSetting(Settings.MOUSE_WHEEL_SWITCH));
		soundVolumeSlider.setValue(game.getSoundVolume());
		musicVolumeSlider.setValue(game.getMusicVolume());
		fullscreenBox
				.setChecked(!settings.getBooleanSetting(Settings.WINDOWED));
	}
}